const RATE_PER_NIGHT = 8000;
const ROOMS = ["1001", "1002", "1003", "1004", "1005"];
const STORAGE_KEY = "hrs-front-desk-demo-state";

const els = {
  navButtons: document.querySelectorAll(".nav-button"),
  views: document.querySelectorAll(".view"),
  viewTitle: document.getElementById("viewTitle"),
  messages: document.getElementById("messages"),
  clearMessages: document.getElementById("clearMessages"),
  resetDemo: document.getElementById("resetDemo"),
  reservationForm: document.getElementById("reservationForm"),
  reservationCheckin: document.getElementById("reservationCheckin"),
  reservationCheckout: document.getElementById("reservationCheckout"),
  reservationNights: document.getElementById("reservationNights"),
  reservationPrice: document.getElementById("reservationPrice"),
  reservationAvailability: document.getElementById("reservationAvailability"),
  checkinForm: document.getElementById("checkinForm"),
  checkinReservationNumber: document.getElementById("checkinReservationNumber"),
  checkoutForm: document.getElementById("checkoutForm"),
  checkoutRoomNumber: document.getElementById("checkoutRoomNumber"),
  availableToday: document.getElementById("availableToday"),
  roomBoard: document.getElementById("roomBoard"),
  roomBoardDate: document.getElementById("roomBoardDate"),
  reservationList: document.getElementById("reservationList"),
  reservationCount: document.getElementById("reservationCount"),
};

let state = loadState();

function loadState() {
  const saved = localStorage.getItem(STORAGE_KEY);
  if (!saved) {
    return { reservations: [] };
  }
  try {
    const parsed = JSON.parse(saved);
    return { reservations: Array.isArray(parsed.reservations) ? parsed.reservations : [] };
  } catch (error) {
    return { reservations: [] };
  }
}

function saveState() {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
}

function todayString() {
  const now = new Date();
  return formatDate(now);
}

function formatDate(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
}

function displayDate(value) {
  return value.replaceAll("-", "/");
}

function addDays(value, amount) {
  const date = new Date(`${value}T00:00:00`);
  date.setDate(date.getDate() + amount);
  return formatDate(date);
}

function dateRange(checkinDate, checkoutDate) {
  const dates = [];
  let cursor = checkinDate;
  while (cursor < checkoutDate) {
    dates.push(cursor);
    cursor = addDays(cursor, 1);
  }
  return dates;
}

function nightsBetween(checkinDate, checkoutDate) {
  if (!checkinDate || !checkoutDate || checkinDate >= checkoutDate) {
    return 0;
  }
  return dateRange(checkinDate, checkoutDate).length;
}

function activeReservations() {
  return state.reservations.filter((reservation) => reservation.status !== "checked-out");
}

function usedCountForDate(date) {
  return activeReservations().filter((reservation) =>
    date >= reservation.checkinDate && date < reservation.checkoutDate
  ).length;
}

function availableCountForPeriod(checkinDate, checkoutDate) {
  const dates = dateRange(checkinDate, checkoutDate);
  if (dates.length === 0) {
    return 0;
  }
  return Math.min(...dates.map((date) => ROOMS.length - usedCountForDate(date)));
}

function occupiedRoomsForDate(date) {
  return activeReservations()
    .filter((reservation) =>
      reservation.roomNumber && date >= reservation.checkinDate && date < reservation.checkoutDate
    )
    .map((reservation) => reservation.roomNumber);
}

function findAvailableRoom(checkinDate, checkoutDate) {
  const dates = dateRange(checkinDate, checkoutDate);
  return ROOMS.find((room) =>
    dates.every((date) => !occupiedRoomsForDate(date).includes(room))
  );
}

function appendMessage(lines, isError = false) {
  const text = Array.isArray(lines) ? lines.join("\n") : lines;
  els.messages.textContent += `\n${isError ? "Error occurred.\n" : ""}${text}`;
  els.messages.scrollTop = els.messages.scrollHeight;
}

function setActiveView(name) {
  els.navButtons.forEach((button) => {
    button.classList.toggle("active", button.dataset.view === name);
  });

  els.views.forEach((view) => {
    const active = view.id === `${name}View`;
    view.classList.toggle("active", active);
    if (active) {
      els.viewTitle.textContent = view.dataset.title;
    }
  });
}

function reservationNumber() {
  return String(Date.now());
}

function updateReservationSummary() {
  const checkinDate = els.reservationCheckin.value;
  const checkoutDate = els.reservationCheckout.value;
  const nights = nightsBetween(checkinDate, checkoutDate);
  const price = nights * RATE_PER_NIGHT;
  els.reservationNights.textContent = nights;
  els.reservationPrice.textContent = `${price.toLocaleString()} yen`;

  if (nights === 0) {
    els.reservationAvailability.textContent = "-";
    return;
  }

  const available = availableCountForPeriod(checkinDate, checkoutDate);
  els.reservationAvailability.textContent = `${available} rooms`;
}

function reserveRoom(event) {
  event.preventDefault();
  const checkinDate = els.reservationCheckin.value;
  const checkoutDate = els.reservationCheckout.value;
  const nights = nightsBetween(checkinDate, checkoutDate);

  if (nights <= 0) {
    appendMessage("Failed to reserve\nDetail:\nCheck-out date must be after check-in date.", true);
    return;
  }

  if (availableCountForPeriod(checkinDate, checkoutDate) <= 0) {
    appendMessage("Failed to reserve\nDetail:\nNo vacant room for the selected period.", true);
    return;
  }

  const reservation = {
    reservationNumber: reservationNumber(),
    checkinDate,
    checkoutDate,
    price: nights * RATE_PER_NIGHT,
    status: "reserved",
    roomNumber: "",
  };

  state.reservations.push(reservation);
  saveState();
  els.checkinReservationNumber.value = reservation.reservationNumber;

  appendMessage([
    "Reservation completed.",
    `Check-in date: ${displayDate(checkinDate)}`,
    `Check-out date: ${displayDate(checkoutDate)}`,
    `Price: ${reservation.price.toLocaleString()} yen`,
    `Reservation number: ${reservation.reservationNumber}`,
  ]);

  renderAll();
  setActiveView("checkin");
}

function checkIn(event) {
  event.preventDefault();
  const number = els.checkinReservationNumber.value.trim();
  const reservation = state.reservations.find((item) => item.reservationNumber === number);

  if (!reservation) {
    appendMessage("Failed to check-in\nDetail:\nReservation was not found.", true);
    return;
  }

  if (reservation.status !== "reserved") {
    appendMessage("Failed to check-in\nDetail:\nReservation has already been consumed.", true);
    return;
  }

  const roomNumber = findAvailableRoom(reservation.checkinDate, reservation.checkoutDate);
  if (!roomNumber) {
    appendMessage("Failed to check-in\nDetail:\nNo empty room for the selected period.", true);
    return;
  }

  reservation.status = "checked-in";
  reservation.roomNumber = roomNumber;
  saveState();
  els.checkoutRoomNumber.value = roomNumber;

  appendMessage([
    "Check-in completed.",
    `Room number: ${roomNumber}`,
    `Check-in date: ${displayDate(reservation.checkinDate)}`,
    `Check-out date: ${displayDate(reservation.checkoutDate)}`,
  ]);

  renderAll();
  setActiveView("checkout");
}

function checkOut(event) {
  event.preventDefault();
  const roomNumber = els.checkoutRoomNumber.value.trim();
  const reservation = state.reservations.find((item) =>
    item.status === "checked-in" && item.roomNumber === roomNumber
  );

  if (!reservation) {
    appendMessage("Failed to check-out\nDetail:\nThe room is not occupied.", true);
    return;
  }

  reservation.status = "checked-out";
  saveState();

  appendMessage([
    "Check-out completed.",
    `Room number: ${reservation.roomNumber}`,
    `Check-in date: ${displayDate(reservation.checkinDate)}`,
    `Check-out date: ${displayDate(reservation.checkoutDate)}`,
    `Price: ${reservation.price.toLocaleString()} yen`,
    "Thank you for using our hotel.",
  ]);

  renderAll();
}

function renderRoomBoard() {
  const today = todayString();
  const occupied = occupiedRoomsForDate(today);
  els.roomBoardDate.textContent = displayDate(today);
  els.availableToday.textContent = `${ROOMS.length - occupied.length} / ${ROOMS.length}`;
  els.roomBoard.innerHTML = "";

  ROOMS.forEach((room) => {
    const tile = document.createElement("div");
    tile.className = `room-tile${occupied.includes(room) ? " busy" : ""}`;
    tile.textContent = room;
    els.roomBoard.appendChild(tile);
  });
}

function renderReservationList() {
  const reservations = [...state.reservations].reverse();
  els.reservationCount.textContent = String(reservations.length);
  els.reservationList.innerHTML = "";

  if (reservations.length === 0) {
    const empty = document.createElement("p");
    empty.className = "status-label";
    empty.textContent = "No reservations yet.";
    els.reservationList.appendChild(empty);
    return;
  }

  reservations.forEach((reservation) => {
    const item = document.createElement("article");
    item.className = "reservation-item";
    item.innerHTML = `
      <div>
        <strong>${reservation.reservationNumber}</strong>
        <small>${displayDate(reservation.checkinDate)} - ${displayDate(reservation.checkoutDate)}
          ${reservation.roomNumber ? ` / Room ${reservation.roomNumber}` : ""}</small>
      </div>
      <span class="badge ${reservation.status}">${reservation.status}</span>
    `;
    els.reservationList.appendChild(item);
  });
}

function renderAll() {
  updateReservationSummary();
  renderRoomBoard();
  renderReservationList();
}

function resetDemo() {
  if (!window.confirm("Reset all demo reservations?")) {
    return;
  }
  state = { reservations: [] };
  saveState();
  els.messages.textContent = "System started.";
  renderAll();
}

function initializeDates() {
  const today = todayString();
  els.reservationCheckin.value = today;
  els.reservationCheckout.value = addDays(today, 2);
}

els.navButtons.forEach((button) => {
  button.addEventListener("click", () => setActiveView(button.dataset.view));
});
els.reservationForm.addEventListener("submit", reserveRoom);
els.checkinForm.addEventListener("submit", checkIn);
els.checkoutForm.addEventListener("submit", checkOut);
els.reservationCheckin.addEventListener("change", updateReservationSummary);
els.reservationCheckout.addEventListener("change", updateReservationSummary);
els.clearMessages.addEventListener("click", () => {
  els.messages.textContent = "System started.";
});
els.resetDemo.addEventListener("click", resetDemo);

initializeDates();
renderAll();

# HRS 実装手順（VS Code + GitHub）

このプロジェクトは授業配布の `src.zip` を元に、チェックアウト処理の未実装部分を補完したものです。

## 1. VS Codeで開く
1. ZIPを展開する。
2. VS Codeで展開したフォルダを開く。
3. `src` フォルダが見えることを確認する。

## 2. 実装した箇所
- `src/app/checkout/CheckOutRoomForm.java`
  - `CheckOutRoomControl` を取得し、`roomNumber` を渡して `checkOut` を呼ぶ。
- `src/app/checkout/CheckOutRoomControl.java`
  - `RoomManager.removeCustomer(roomNumber)` で部屋を空け、宿泊日を取得する。
  - `PaymentManager.consumePayment(stayingDate, roomNumber)` で支払いを完了する。

## 3. コンパイル
このコードは一部コメントが Shift-JIS/MS932 系なので、Windowsでは通常どおり、ターミナルでは次のようにコンパイルする。

```bash
mkdir bin
javac -encoding MS932 -d bin $(find src -name "*.java")
```

Windows PowerShell の場合は次のようにする。

```powershell
mkdir bin
javac -encoding MS932 -d bin (Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName })
```

## 4. 実行
DBサーバーが起動している状態で、次を実行する。

```bash
java -cp "bin;lib/hsqldb.jar" app.cui.CUI
```

Linux/Macの場合は区切り文字を `:` にする。

```bash
java -cp "bin:lib/hsqldb.jar" app.cui.CUI
```

## 5. 注意
添付されていた `hsqldb.jar` は実体が jar ではなく Eclipse の設定ファイルのように見えました。実行には授業配布の本物の `lib/hsqldb.jar` と `dev_program_DB/mydb/runServer.bat` が必要です。

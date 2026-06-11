name: Build DJI Scout APK

on:
  push:
    branches: [ main, master ]
  pull_request:
    branches: [ main ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout código
        uses: actions/checkout@v4

      - name: Configurar JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: gradle

      - name: Configurar Android SDK
        uses: android-actions/setup-android@v3

      - name: Dar permisos a gradlew
        run: chmod +x ./gradlew

      - name: Cache Gradle
        uses: actions/cache@v4
        with:
          path: |
            ~/.gradle/caches
            ~/.gradle/wrapper
          key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
          restore-keys: |
            ${{ runner.os }}-gradle-

      - name: Compilar APK Debug
        run: ./gradlew assembleDebug --no-daemon --stacktrace

      - name: Subir APK como artefacto
        uses: actions/upload-artifact@v4
        with:
          name: DJI-Scout-Debug-APK
          path: app/build/outputs/apk/debug/app-debug.apk
          retention-days: 30

      - name: Mostrar info del APK
        run: |
          echo "=== APK generado ==="
          ls -lh app/build/outputs/apk/debug/

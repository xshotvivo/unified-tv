#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BUILD_DIR="$ROOT/.build/core-domain"
mkdir -p "$BUILD_DIR"

kotlinc \
  "$ROOT/core-domain/src/main/kotlin/com/originostv/utv/music/domain/MusicResource.kt" \
  "$ROOT/core-domain/src/test/kotlin/com/originostv/utv/music/domain/ContractSmokeTest.kt" \
  -include-runtime \
  -d "$BUILD_DIR/domain-tests.jar"

java -jar "$BUILD_DIR/domain-tests.jar"

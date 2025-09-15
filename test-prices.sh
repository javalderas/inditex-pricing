#!/usr/bin/env bash
set -euo pipefail

BASE_URL="http://localhost:8080/api/v1/prices"

pretty_json() {
  if command -v jq >/dev/null 2>&1; then
    jq
  elif command -v python3 >/dev/null 2>&1; then
    python3 -m json.tool
  else
    cat
  fi
}

call_test() {
  local when="$1"
  echo "🔎 Test: $when"
  curl -sS "${BASE_URL}?productId=35455&brandId=1&applicationDate=${when}" | pretty_json
  echo -e "\n"
}

call_test "2020-06-14T10:00:00Z"  # Expected: priceList 1, amount 35.50
call_test "2020-06-14T16:00:00Z"  # Expected: priceList 2, amount 25.45
call_test "2020-06-14T21:00:00Z"  # Expected: priceList 1, amount 35.50
call_test "2020-06-15T10:00:00Z"  # Expected: priceList 3, amount 30.50
call_test "2020-06-16T21:00:00Z"  # Expected: priceList 4, amount 38.95
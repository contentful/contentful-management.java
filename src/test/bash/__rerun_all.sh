#!/usr/bin/env bash

THIS_SCRIPT="$(basename $0 .sh)"

for f in $(ls); do
    CURRENT="$(basename ${f} .sh)"
    if [ "${CURRENT}" != "$THIS_SCRIPT" ]; then
      echo ${CURRENT}
      ./${CURRENT}.sh
    fi
done
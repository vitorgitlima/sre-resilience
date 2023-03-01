#!/bin/bash

running=true

# Loop infinito que executa o script continuamente até ser interrompido manualmente
while [ "$running" = true ]; do

  # Loop que varia a quantidade de chamadas simultâneas em cada etapa
  for simultaneous_calls in 10 2 30 5; do

    # Exibe uma mensagem indicando a quantidade de chamadas simultâneas
    echo "Realizando $simultaneous_calls chamadas simultâneas"

    # Loop que executa as chamadas com a quantidade definida de processos simultâneos
    for i in $(seq 1 $simultaneous_calls); do
      # Realiza a chamada GET para o endpoint com um ID aleatório, em background
      curl -X GET "http://localhost:8080/api/sre/v1/getDemoSRE/$(shuf -i 1-10 -n 1)" &
    done

    # Aguarda 500ms antes de executar a próxima etapa
    sleep 0.5
  done
done

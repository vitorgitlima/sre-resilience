#!/bin/bash

# ***************************************************************************************************************************
# Uso:
# -m: Quantidade máxima de memória que será disponibilizada ao POD e a JVM (Default: 512m)
# -r: Quantidade inicial de memória (Memória reservada) que será disponibilizada ao POD e a JVM (Default: 256m)
# -b: Informa se a imagem deve ser construída durante a execução do script. Caso deseje modificar algum dos outros parâmetros (exemplo memória) esta opção deverá ser TRUE (Default: false)
# -c: Limita a quantidade de CPU do POD, a quantidade máxima não poderá exceder o número de núcleos da maquina host (Default: sem limitação)
# ***************************************************************************************************************************

max_memory=512m
reservation=256m
build_image=true
container_image=opin-srv-app
container_name=$container_image-test
container_id=""
base_path=$(dirname "$(dirname "$(dirname $(readlink -f "$0"))")")
max_cpus=0
cpus_args=""

while getopts m:r:b:c: flag
do
    case "${flag}" in
        m) max_memory=${OPTARG};;
        r) reservation=${OPTARG};;
        c) max_cpus=${OPTARG};;
        b)
          if [[ "$OPTARG" == "true" || "$OPTARG" == "false" ]]; then
                  # Se for, atualiza o valor da variável booleana
                  build_image="$OPTARG"
                else
                  # Se não for, exibe uma mensagem de erro e sai
                  echo "O valor da opção -b deve ser 'true' ou 'false'."
                  exit 1
                fi
                ;;
        \?)
            echo "Opção inválida: -$OPTARG" >&2
            exit 1
            ;;
        :)
            echo "A opção -$OPTARG requer um argumento." >&2
            exit 1
            ;;
    esac
done

# Identifica se o parâmetro de cpus foi utilizado
if [ "$max_cpus" -gt 0 ]; then
  cpus_args="--cpus=$max_cpus"
fi

# Faz o build da imagem caso solicitado pelo usuário (através da flag -b)
if [ "$build_image" = true ]; then
  echo -e "\033[34mExecutando DOCKER BUILD com os parametros  --file ${base_path}/Dockerfile min_memory=$reservation max_memory=$max_memory $cpus_args\033[0m"
  docker build --file "${base_path}/Dockerfile" --build-arg min_memory="$reservation" --build-arg max_memory="$max_memory" -t "$container_image" "$base_path"

  if [ $? -ne 0 ]; then
    echo -e "\033[31mErro ao realizar o build da imagem '$container_image'.\033[0m"
    exit 1
  fi

  echo -e "\033[32mImagem '$container_image' construída com sucesso.\033[0m"
fi

# Caso exista, remove o container (que está na variável $container_name) do teste anterior
docker container ls -aq -f "name=$container_name" | xargs docker rm -f "{}" 2> /dev/null

#Sobe o container com os parâmetros informados
echo -e "\033[34mExecutando DOCKER RUN com os parametros -m '$max_memory' --memory-reservation='$reservation' $cpus_args --name '$container_name' '$container_image'\033[0m"
docker run -id \
	-p 8080:8080 \
	-m "$max_memory" \
	--memory-reservation="$reservation" \
	-e MONGO_HOST='docker_mongo_1' \
	-e REDIS_HOST='docker_redis_1' \
	--network='docker_vpcopeninsurance' \
	--name $container_name \
  --cpus="$max_cpus" \
	$container_image

#-p 9010:9010 \

container_id=$(docker ps -q -f "name=$container_name")

echo -e "\033[32mContainer '$container_name' rodando com sucesso. Container ID: '$container_id'\033[0m"
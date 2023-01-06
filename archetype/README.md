# Instalação do archetype

Para a criação de um novo projeto utilizando o archetype, primeiro precisamos fazer o install do mesmo dentro do nexus local (visto que não é possível adicionar no nexus central).

Para isso, estando na raiz do projeto, executar o seguinte comando

`mvn clean install`

Está configurado atualmente para gerar sempre com a versão _1.0-SNAPSHOT_, mas em caso de ser necessário uma versão diferente mantendo a anterior pode-se alterar livremente.


# Criação de um novo projeto

Após o archetype instalado, podemos criar um projeto com todas as configurações pré-definidas e arquivos padrões já criados.

Para isso, iremos utilizar o comando para a criação do novo projeto, passando os parâmetros que são necessários.

`mvn archetype:generate -D<parametro>=<valorParametro>`

**PS. O comando acima deve ser executado em uma pasta que não possua um arquivo pom.xml**


# Parâmetros para esse archetype

Os parâmetros abaixo são os requeridos para esse archetype, portanto devem ser passados na geração de um novo projeto:

- -DarchetypeGroupId=**_groupId do projeto archetype_**
- -DarchetypeArtifactId=**_artifactId do projeto archetype_**
- -DarchetypeVersion=**_versão do projeto archetype_**
- -DgroupId=**_groupId do novo projeto_**
- -DartifactId=**_artifactId do novo projeto_**
- -Dversion=**_versão inicial do novo projeto_**
- -DswaggerName=**_nome do swagger do novo projeto. Deverá ser adicionado aos resources após a criação_**
- -Ddescription=**_descrição do que se refere o novo projeto_**
- -Dyear=**_ano de criação do novo projeto_**

Exemplo do comando completo para o projeto do chassi:

> mvn archetype:generate -DarchetypeGroupId=br.com.bradescoseguros.opin -DarchetypeArtifactId=opin-srv-re-produtos-archetype -DarchetypeVersion=1.0-SNAPSHOT -DgroupId=br.com.bradescoseguros.opin -DartifactId=opin-srv-re-produtos-chassis -Dversion=1.0 -DswaggerName=20211125_GT_Tecnologia_e_Infraestrutura_Documento_Tecnico_API_Residencial.yaml -Ddescription="Fornecimento de Produtos Residenciais" -Dyear=2021

**PS. Em caso de ser necessário a utilização de espaços em um dos parâmetros, colocoar o valor entre aspas ""**


# Utilização do projeto gerado

Após a execução do passo acima, o projeto gerado já está pronto para ser usado no desenvolvimento, restando apenas conferir se algo fora do padrão gerado não é necessário:

- Conferir se todas as configurações necessárias no projeto específico estão corretas
- Adicionar o arquivo .yaml do swagger na pasta específica
- Excluir os arquivos com o nome _"Dummy"_, pois estes não são utilizados após a geração do projeto.

**Para exemplos de código para o desenvolvimento da API, verificar o que está disponibilizado do projeto de chassi.**
# XY Inc Teste Zup Pontos de Interesses (POIs) #

### Dependências ###

As seguintes dependências para o projeto são necessárias:

- [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (versão 8)
- [Maven](https://maven.apache.org/) (versão 3.3 ou maior)
- [MySQL]() - servidor de banco de dados;


### Configurando o banco MYSQL ###

- Crie 1 banco de dados de acordo com as configurações contindas em: src/main/resources/application.properties

#### API REST ####

A aplicação REST do servidor expõe os seguintes serviços:

##### Pontos de Interesse #####

Url           |HTTP Verb        |Request Body  | Descrição
--------------|------------- |------------- | -------------
/rest/pontos-de-interesses       |GET          |void|lista todos os pontos de interesses cadastrados na aplicação.
/rest/pontos-de-interesses |POST|JSON| cria um novo ou mais pontos de interesses, a partir de uma lista.
/rest/pontos-de-interesses/proximidade?pX={x}&pY={y}&distance={distance}|GET|void| busca pontos de interesse baseado em uma localização `x`, `y` e uma distância máxima (`distance`).

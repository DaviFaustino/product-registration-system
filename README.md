<h1 align="center" style="font-weight: bold;">Product Registration System</h1>

<p align="center">
  <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white" alt="java"/>
  <img src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white" alt="spring"/>
  <img src="https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white" alt="Apache Maven"/>
  <img src="https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white" alt="Postgres"/>
  <img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white" alt="Docker"/>
  <img src="https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white" alt="Swagger"/>
</p>

<p align="center">
  <a href="#perfis">Perfis</a> • 
  <a href="#variaveis">Variáveis de ambiente</a> • 
  <a href="#executar">Como executar</a> • 
  <a href="#routes">API Endpoints</a> •
  <a href="#swagger">Swagger</a>
</p>

<p>
  Este projeto é o backend de um sistema de registro de produtos onde são
  registrados informações como nome, preço, descrição, dentre outros dados.
  O controle de estoque é mínimo. O foco principal do projeto está em registrar
  dados dos produtos para consulta.
</p>

<h2 id="perfis">Perfis</h2>

<b>O projeto possui 3 perfis maven:</b>

<b>development (default) </b>- utiliza o H2 como banco de dados em memória, devtools, jacoco para cobertura de código e swagger ui habilitado.  
<b>homologation </b>- utiliza o PostgreSQL como banco de dados, jacoco para cobertura de código e swagger ui habilitado.  
<b>production </b>- utiliza o PostgreSQL como banco de dados e swagger ui desabilitado.

<h2 id="variaveis">Variáveis de ambiente</h2>

**DB_USERNAME:** guarda o nome de usuário para acessar o postgres nos perfis homologation e production.  
**DB_PASSWORD:** guarda a senha para acessar o postgres nos perfis homologation e production.  
**FRONTEND_URL:** guarda a url do frontend para configurar o CORS.

<h2 id="executar">Como executar</h2>

<h3>Clonar projeto</h3>

Utilize o [Git](https://git-scm.com/) para clonar o projeto e em seguida acesse o seu diretório.
```bash
git clone https://github.com/DaviFaustino/product-registration-system-backend.git
```

<h3 id="executar-development">Executar: com banco de dados em memória sem container</h3>

<h4>Requisitos</h4>

- Java 17
- Maven

<h4>Comandos</h4>

```bash
mvn spring-boot:run
```

<h3 id="executar-homologation">Executar: com banco de dados em memória com container</h3>

<h4>Requisitos</h4>

- Docker

<h4>Comandos</h4>

```bash
#Fazer o build da imagem docker do projeto
docker build --build-arg MAVEN_PROFILE=development -t product-registration-app .
#Rodar container
docker run --name product-registration-app --network host -d product-registration-app
```

<h3>Executar: com o banco PostgreSQL com container</h3>

<h4>Requisitos</h4>

- Docker
- Docker Compose

<h4>Variáveis arquivo .env</h4>

O projeto possui um arquivo ***.env*** na raiz, o qual você pode alterar com os valores para setar as variáveis de ambiente da 
aplicação e do postgres no arquivo docker-compose.yml. 
USER seta DB_USERNAME da aplicação e POSTGRES_USER do banco de dados. PASSWORD seta DB_PASSWORD da aplicação e 
POSTGRES_PASSWORD do banco de dados. FRONTEND_URL seta FRONTEND_URL da aplicação.

```dotenv
USER=postgres
PASSWORD=postgres
FRONTEND_URL=http://localhost:80
```

<h4>Comandos</h4>

```bash
#Fazer o build com perfil production
docker compose build --no-cache
#ou se deseja ultilizar o swagger ui, faça o build com homologation
docker compose build --build-arg MAVEN_PROFILE=homologation --no-cache

#Rodar containers
docker compose up -d
```

<h2 id="routes">API Endpoints</h2>

| método/rota | query params | descrição | body |
|------|--------------|-----------|-----|
| <kbd>GET /product-types</kbd> | searchTerm, category | Obtém uma lista de produtos | [json](#get-product-types) |
| <kbd>POST /product-types</kbd> | - | Cria um novo produto | [json](#post-product-types) |
| <kbd>DELETE /product-types/{name}</kbd> | - | Deleta um produto | - |
| <kbd>PATCH /product-types/{name}</kbd> | - | Atualiza um produto | [json](#patch-product-types) |
| <kbd>GET /product-types/names</kbd> | - | Obtém uma lista de produtos com preços atualizados recentemente | [json](#get-product-types-names) |
| <kbd>GET /products</kbd> | searchTerm, productTypeName | Obtém uma lista de tipos de produto | [json](#get-products) |
| <kbd>POST /products</kbd> | isPriceOld | Cria um novo tipo de produto | [json](#post-products) |
| <kbd>DELETE /products</kbd> | code | Deleta um tipo de produto | - |
| <kbd>PATCH /products</kbd> | code | Atualiza um tipo de produto | [json](#patch-products) |
| <kbd>GET /products/recent-price-updates</kbd> | initialTime | Obtém uma lista de nomes de tipos de produto | [json](#get-products-recent-price-updates) |

<h2>Exemplos de requests e responses</h2>

<h3 id="get-product-types">GET /product-types</h3>

**Response**
```json
[
  {
    "name": "string",
    "category": "BAKING",
    "averagePriceInCents": 100,
    "fullStockFactor": 1
  }
]
```
-----
<h3 id="post-product-types">POST /product-types</h3>

**Request**
```json
{
  "name": "string",
  "category": "BAKING",
  "fullStockFactor": 0
}
```

**Response**
```json
{
  "name": "string",
  "category": "BAKING",
  "averagePriceInCents": null,
  "fullStockFactor": 0
}
```
-----
<h3 id="patch-product-types">PATCH /product-types</h3>

**Request**
```json
{
  "name": "string",
  "category": "BAKING",
  "fullStockFactor": 0
}
```

**Response**
```json
1
```
-----
<h3 id="get-product-types-names">GET /product-types/names</h3>

**Response**
```json
[
  "string"
]
```
-----
<h3 id="get-products">GET /products</h3>

**Response**
```json
[
  {
    "code": "string",
    "productTypeName": "string",
    "name": "string",
    "description": "string",
    "purchasePriceInCents": 0,
    "previousPurchasePriceInCents": 0,
    "salePriceInCents": 0,
    "previousSalePriceInCents": 0,
    "priceUpdateDate": 1705200000000,
    "fullStock": true
  }
]
```
-----
<h3 id="post-products">POST /products</h3>

**Request**
```json
{
  "code": "string",
  "productTypeName": "string",
  "name": "string",
  "description": "string",
  "purchasePriceInCents": 0,
  "salePriceInCents": 0,
  "fullStock": true
}
```

**Response**
```json
{
  "code": "string",
  "productTypeName": "string",
  "name": "string",
  "description": "string",
  "purchasePriceInCents": 0,
  "previousPurchasePriceInCents": 0,
  "salePriceInCents": 0,
  "previousSalePriceInCents": 0,
  "priceUpdateDate": 1705200000000,
  "fullStock": true
}
```
-----
<h3 id="patch-products">PATCH /products</h3>

**Request**
```json
{
  "code": "string",
  "productTypeName": "string",
  "name": "string",
  "description": "string",
  "purchasePriceInCents": 0,
  "salePriceInCents": 0,
  "fullStock": true
}
```

**Response**
```json
1
```
-----
<h3 id="get-products-recent-price-updates">GET /products/recent-price-updates</h3>

**Response**
```json
[
  "string"
]
```

<h2 id="swagger">Swagger</h2>

Para mais detalhes da api e realizar testes, execute a aplicação localmente com perfil [development](#executar-development) 
ou [homologation](#executar-homologation) e acesse o swagger ui em <a>http://localhost:8080/swagger-ui/index.html#/</a>.

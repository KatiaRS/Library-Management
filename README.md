<div align="center">
  
![imagem da biblioteca](https://images-cdn.reedsy.com/discovery/post/109/featured_image/medium_aa7b8fcc4ee3a86626aca3157bbd8d697c38429a.jpg)

# API para Sistema de Gerentiamento de Biblioteca


</div>

###  Descrição do Projeto
Esse é um projeto básico que tem como objetivo, permitir que os admnistradores da biblioteca gerenciem os livros a partir de uma API Rest, facilitando operações como
adição de livros, consulta dos mesmos, edição e remoção. Cada livro possui informações como um ID no formato UUID gerado automaticamente, autor, título e um ISBN.

### Tecnologias

* Linguagem de programação: Kotlin :books:
* JDK : Java 11 :coffee:
* Estrutura : SpringBoot
* Gerenciamento de Dependências : Gradle
* Spring Boot Data JPA :rocket:
* Spring Boot Validation :white_check_mark:
* Spring Boot Web :computer:
* Insomnia :large_blue_circle:	

### Banco de Dados
* H2 Database 	:package:

## Funcionalidades :hammer_and_wrench:

* POST/books: Adiciona um novo livro à biblioteca
* GET/books: Retorna uma lista de todos os livros cadastrados
* GET/books/{id}: Retorna informações de um livro específico pelo ID
* GET/books/param = [title]: Retorna informações de um livro específico pelo title
* GET/books/param = [author]: Retorna uma lista de livros daquele author específico
* GET/books/param = [isbn]: Retorna informações de um livro específico pelo isbn
* PUT/books/{id}: Atualiza as informações de um livro existente
* DELETE/book/{id}: Remove um livro da biblioteca



## Instalação :gear:

1. Certifique se você tem o Kotlin instalado em sua máquina
2. Acesse o GitHub e clone o repositório: git clone `https://github.com:KatiaRS/Library-Management.git`
3. Abra o terminal e acesse o diretório do projeto: `cd library-Management`
4. Execute o projeto no IntelliJ IDEA
5. Suba a aplicação e certifique se estará disponível em `http://localhost:8080`

## Endpoints da API 	:round_pushpin:

* Cadastro de Livro: POST/books`
  * Corpo da requisição (Exemplo)
    * a) Request:
     * ![image](https://github.com/KatiaRS/Library-Management/assets/115504238/fd050742-6fd9-4863-8ceb-c9ed459208ea)

    * b) Response:
     * ![image](https://github.com/KatiaRS/Library-Management/assets/115504238/5bdd9f54-e0d3-4953-9a4c-77ab0ccc2c0c)

* Busca de uma lista de livros cadastrados: GET/books
   * Na request não deve passar o body, somente passar books na URL
    * a) Request:
      * ![image](https://github.com/KatiaRS/Library-Management/assets/115504238/1c58f94a-1ee3-4954-819f-3a59d593b3f7)

    
    * b) Response:     
      * ![image](https://github.com/KatiaRS/Library-Management/assets/115504238/3e078713-77f8-495f-a50e-fa5d1dabfa2d)

* Busca de um livro específico por id: GET/books/{id}
   * Na request não deve passar o body, somente passar id na URL
     * a) Request:
        * ![image](https://github.com/KatiaRS/Library-Management/assets/115504238/ef6d48de-2234-422a-9724-0946da8b8c5e)

        
      * b) Response:     
        * ![image](https://github.com/KatiaRS/Library-Management/assets/115504238/64673ec7-6301-491e-b87f-0ac74429ce25)
     
* Busca de um livro específico por título: GET/books?title=title
   * Na request não deve passar o body, somente passar título como parâmetro na URL
     * a) Request:
        * ![image](https://github.com/KatiaRS/Library-Management/assets/115504238/975d6554-ed80-4b2e-840f-221689ec1fae)

  
      
      * b) Response:     
        * ![image](https://github.com/KatiaRS/Library-Management/assets/115504238/45b0bb79-1538-453e-8c79-34be8543b192)
      

* Busca de um livro específico por author: GET/books?author=auhtor
   * Na request não deve passar o body, somente passar author como parâmetro na URL
     * a) Request:
        * ![image](https://github.com/KatiaRS/Library-Management/assets/115504238/f4bfb166-a376-451e-a21a-e98318bfb28d)

        
      * b) Response:     
        * ![image](https://github.com/KatiaRS/Library-Management/assets/115504238/b233f352-769f-41a5-a1d8-381c9c83ea57)
              

* Busca de um livro específico por isbn: GET/books?isbn=isbn
   * Na request não deve passar o body, somente passar isbn como parâmetro na URL
     * a) Request:
        * ![image](https://github.com/KatiaRS/Library-Management/assets/115504238/465d5d16-48b7-4632-b3b5-e948fc67f81a)

        
      * b) Response:
        * ![image](https://github.com/KatiaRS/Library-Management/assets/115504238/ece3444b-1b6d-4be9-995b-65ed2e63d107)
   
    
* Alteração de infomrações de um livro específico por id: PUT/books/{id}
   * Na request não deve passar o body, somente passar id na URL
     * a) Request:
        * ![image](https://github.com/KatiaRS/Library-Management/assets/115504238/6b893a7d-254c-4555-9521-a9b4abfc668d)
        * ![image](https://github.com/KatiaRS/Library-Management/assets/115504238/919383a9-4d83-4766-9d28-6424e6864a32)
        * Exemplo antes de alterar

     * b) Response: 
        * Exemplo depois de alterar
        * ![image](https://github.com/KatiaRS/Library-Management/assets/115504238/0d3fa03c-9bd6-4d70-b297-bcb129cbb45d)
        * ![image](https://github.com/KatiaRS/Library-Management/assets/115504238/77313d6f-d569-4e5e-9d01-51ad4081829a)
      
     

* Remover um livro específico por id: DELETE/books/{id}
   * Na request não deve passar o body, somente passar id na URL
     * a) Request:
        * ![image](https://github.com/KatiaRS/Library-Management/assets/115504238/15ecd51d-3bca-4b1e-ace3-655da0b7750a)

  
      
     * b) Response: 
        * ![image](https://github.com/KatiaRS/Library-Management/assets/115504238/3b3fc5c1-f713-4ca4-a6b2-4b9293065398)


## Projeto com Padrão DDD e Rest
### Domain Driven Design (DDD)

Este projeto segue o padrão Domain-Driven Design (DDD), uma abordagem que visa alinhar o desenvolvimento de software com os
conceitos fundamentais do domínio do problema. Aqui estão alguns pontos-chave sobre o uso do DDD neste projeto, especialmente
em um contexto REST:

#### Principais Conceitos do DDD:
* Entidades: Representam objetos com identidade própria ao longo do tempo.
* Value Objects: Objetos definidos apenas por seus atributos, sem identidade própria.
* Agregados: Grupos de entidades e value objects tratados como uma única unidade.
* Repositórios: Camada de acesso a dados para recuperar ou armazenar entidades.
* Serviços de Domínio: Classes que contêm lógica de negócio específica.

#### Vantagens no Contexto REST:
* Modelagem Rica: Reflete os recursos da API REST, melhorando a representação do domínio.
* Manutenção Simplificada: Responsabilidades bem definidas facilitam a manutenção do código.
* Escalabilidade: Modelagem baseada em agregados e limites de contexto favorecem a escalabilidade.
* Compreensão do Negócio: Facilita a comunicação entre desenvolvedores e stakeholders, proporcionando
uma compreensão mais profunda do domínio.











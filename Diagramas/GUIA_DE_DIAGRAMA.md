## Diagrama de Classe:

classDiagram
    %% ===========================
    %% Interface Usuario
    %% ===========================
    class Usuario {
        <<interface>>
        +String email
        +String senhaHash
        +String getEmail()
        +String getSenhaHash()
        +void setSenha(String senhaPura)
        +boolean autenticar(String senhaPura)
    }

    %% ===========================
    %% Classe Credor
    %% ===========================
    class Credor {
        Long id
        String nome
        String documento
        String email
        String senhaHash
        List<Oferta> ofertas
        List<PropostaEmprestimo> propostasCriadas
        List<Emprestimo> emprestimosRealizados

        +criarOferta(Oferta o)
        +deletarOferta(Long ofertaId)
        +criarProposta(Oferta oferta)
        +listarPropostas()
    }
    Usuario <|.. Credor

    %% ===========================
    %% Classe Devedor
    %% ===========================
    class Devedor {
        Long id
        String nome
        String cpf
        String telefone
        String endereco
        String email
        String senhaHash
        List<Emprestimo> emprestimosContratados
        List<PropostaEmprestimo> propostasDesejadas

        +buscarPropostas()
        +aceitarTermos(Long propostaId)
        +pedirEmprestimo(PropostaEmprestimo proposta)
    }
    Usuario <|.. Devedor

    %% ===========================
    %% Classe Oferta
    %% ===========================
    class Oferta {
        Long id
        Double valor
        Integer maxParcelas
        Integer prazoDiasPrimeiraParcela
        Double jurosPercentual
        LocalDate dataCriacao
        Double valorParcela
        LocalDate dataPrimeiraParcela

        +calcularParcelas()
        +gerarProposta()
    }

    Credor "1" --> "0..*" Oferta : cria >

    %% ===========================
    %% Classe PropostaEmprestimo
    %% ===========================
    class PropostaEmprestimo {
        Long id
        String idPublico
        Boolean aceitaTermos
        List<Devedor> interessados

        +registrarInteresse(Devedor d)
        +aceitarTermos(Devedor d)
        +aprovarParaEmprestimo(Devedor d)
    }

    Oferta "1" --> "1" PropostaEmprestimo : origem >
    Credor "1" --> "0..*" PropostaEmprestimo : cria >
    PropostaEmprestimo "0..*" --> "0..*" Devedor : interessados >

    %% ===========================
    %% Classe Emprestimo
    %% ===========================
    class Emprestimo {
        Long id
        LocalDate dataInicio
        List<Parcela> parcelas
        StatusEmprestimo status

        +atualizarStatus()
        +registrarPagamentoParcela(numeroParcela)
        +verificarAtraso()
    }

    Credor "1" --> "0..*" Emprestimo : realiza >
    Devedor "1" --> "0..*" Emprestimo : contrata >
    PropostaEmprestimo "1" --> "1" Emprestimo : gera >

    %% ===========================
    %% Classe Parcela
    %% ===========================
    class Parcela {
        Long id
        Integer numero
        Double valor
        LocalDate dataVencimento
        Boolean paga

        +marcarComoPaga()
        +isAtrasada()
    }

    Emprestimo "1" --> "0..*" Parcela : possui >

    %% ===========================
    %% ENUM
    %% ===========================
    class StatusEmprestimo {
        <<enumeration>>
        EM_ANDAMENTO
        ATRASADO
        PAGO
    }







## Diagrama de Caso de Uso - PlantUML:

@startuml
left to right direction
skinparam packageStyle rectangle
skinparam usecase {
  BackgroundColor #EEF2F7
  BorderColor #4A6FA5
}

actor Credor
actor Devedor
actor "Sistema de Empréstimos" as Sistema

rectangle "Gerenciamento de Contas" {
    usecase "Criar Conta" as UC1
    usecase "Fazer Login" as UC2
}

rectangle "Operações do Credor" {
    usecase "Criar Oferta de Empréstimo" as UC3
    usecase "Gerar Proposta de\nEmpréstimo" as UC4
    usecase "Definir Juros\ne Validade" as UC5
}

rectangle "Operações do Devedor" {
    usecase "Buscar Propostas\n de Empréstimo" as UC6
    usecase "Selecionar Proposta" as UC7
    usecase "Fazer Pedido de\nEmpréstimo" as UC8
    usecase "Aceitar Termos" as UC9
}

rectangle "Processo de Empréstimo" {
    usecase "Registrar Empréstimo" as UC10
}

' Relações dos Atores
Credor --> UC1
Credor --> UC2
Credor --> UC3
Credor --> UC4
Credor --> UC10

Devedor --> UC1
Devedor --> UC2
Devedor --> UC6
Devedor --> UC7
Devedor --> UC8
Devedor --> UC9
Devedor --> UC10

' Relações Include
UC4 .> UC5 : <<include>>
UC8 .> UC10 : <<include>>

@enduml

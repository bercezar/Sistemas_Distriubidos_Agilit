# 📚 DOCUMENTAÇÃO COMPLETA DA API - Sistema de Empréstimos Agilit

## 🌐 Base URL
```
http://localhost:8080/Sistemas_Distriubidos_Agilit/api
```

---

## 📋 ÍNDICE DE ROTAS

### 👤 Credor
- [POST /api/credor](#post-apicredor) - Criar credor
- [POST /api/credor/criar-conta](#post-apicredorcriar-conta) - Criar conta com validação
- [GET /api/credor](#get-apicredor) - Listar todos credores
- [GET /api/credor/{id}](#get-apicredor-id) - Buscar credor por ID
- [PUT /api/credor/{id}](#put-apicredor-id) - Atualizar credor
- [DELETE /api/credor/{id}](#delete-apicredor-id) - Deletar credor
- [PUT /api/credor/saldo/{id}](#put-apicredorsaldo-id) - Registrar saldo

### 👥 Devedor
- [POST /api/devedor](#post-apidevedor) - Criar devedor
- [GET /api/devedor](#get-apidevedor) - Listar todos devedores
- [GET /api/devedor/{id}](#get-apidevedor-id) - Buscar devedor por ID

### 💰 Oferta de Empréstimo
- [POST /api/oferta](#post-apioferta) - Criar oferta (privada)
- [GET /api/oferta/credor/{credorId}](#get-apiofertacredorcredorid) - Listar ofertas do credor
- [GET /api/oferta/credor/{credorId}/ativas](#get-apiofertacredorcredoridativas) - Listar ofertas ativas
- [GET /api/oferta/{id}](#get-apioferta-id) - Buscar oferta por ID
- [DELETE /api/oferta/{id}](#delete-apioferta-id) - Deletar oferta
- [POST /api/oferta/{id}/criar-proposta](#post-apioferta-idcriar-proposta) - Criar proposta pública
- [GET /api/oferta/{id}/opcoes-parcelas](#get-apioferta-idopcoes-parcelas) - Calcular opções

### 📢 Proposta de Empréstimo
- [GET /api/proposta/publicas](#get-apipropostapublicas) - Listar propostas públicas ativas
- [GET /api/proposta/credor/{credorId}](#get-apipropostacredorcredorid) - Propostas do credor
- [GET /api/proposta/publico/{idPublico}](#get-apipropostapublicoidpublico) - Buscar por ID público
- [GET /api/proposta/{id}](#get-apiproposta-id) - Buscar por ID
- [PUT /api/proposta/{id}/cancelar](#put-apiproposta-idcancelar) - Cancelar proposta
- [GET /api/proposta/{id}/detalhes](#get-apiproposta-iddetalhes) - Detalhes completos
- [GET /api/proposta/status/{status}](#get-apipropostastatusstatus) - Filtrar por status
- [GET /api/proposta/buscar](#get-apipropostabuscar) - Buscar por valor
- [GET /api/proposta/{id}/estatisticas](#get-apiproposta-idestatisticas) - Estatísticas

### ❤️ Interesse em Proposta
- [POST /api/interesse](#post-apiinteresse) - Demonstrar interesse
- [GET /api/interesse/proposta/{propostaId}](#get-apiinteressepropostapropostaid) - Listar interessados
- [GET /api/interesse/devedor/{devedorId}](#get-apiinteressedevedordevedorid) - Meus interesses
- [PUT /api/interesse/{id}/aprovar](#get-apiinteresse-idaprovar) - Credor aprova
- [PUT /api/interesse/{id}/rejeitar](#put-apiinteresse-idrejeitar) - Credor rejeita
- [POST /api/interesse/{id}/confirmar-credor](#post-apiinteresse-idconfirmar-credor) - Credor confirma
- [POST /api/interesse/{id}/confirmar-devedor](#post-apiinteresse-idconfirmar-devedor) - Devedor confirma
- [DELETE /api/interesse/{id}](#delete-apiinteresse-id) - Cancelar interesse

### 💳 Empréstimo
- [GET /api/emprestimo](#get-apiemprestimo) - Listar todos
- [GET /api/emprestimo/{id}](#get-apiemprestimo-id) - Buscar por ID
- [POST /api/emprestimo](#post-apiemprestimo) - Criar empréstimo
- [PUT /api/emprestimo/{id}](#put-apiemprestimo-id) - Atualizar empréstimo
- [DELETE /api/emprestimo/{id}](#delete-apiemprestimo-id) - Deletar empréstimo

### 📅 Parcela
- [GET /api/parcela/emprestimo/{emprestimoId}](#get-apiparcelaemprestimoemprestimoid) - Listar parcelas
- [GET /api/parcela/{id}](#get-apiparcela-id) - Buscar parcela
- [PUT /api/parcela/{id}/pagar](#put-apiparcela-idpagar) - Marcar como paga
- [GET /api/parcela/emprestimo/{emprestimoId}/pendentes](#get-apiparcelaemprestimoemprestimoidpendentes) - Parcelas pendentes
- [GET /api/parcela/emprestimo/{emprestimoId}/pagas](#get-apiparcelaemprestimoemprestimoidpagas) - Parcelas pagas
- [GET /api/parcela/emprestimo/{emprestimoId}/atrasadas](#get-apiparcelaemprestimoemprestimoidatrasadas) - Parcelas atrasadas
- [GET /api/parcela/emprestimo/{emprestimoId}/resumo](#get-apiparcelaemprestimoemprestimoidresumo) - Resumo
- [GET /api/parcela/vencidas](#get-apiparcelavencidas) - Todas vencidas
- [GET /api/parcela/emprestimo/{emprestimoId}/proximas-vencimento](#get-apiparcelaemprestimoemprestimoidproximas-vencimento) - Próximas

### 🔔 Notificação
- [GET /api/notificacao/{tipoDestinatario}/{destinatarioId}](#get-apinotificacaotipodestinatariodestinatarioid) - Listar notificações
- [GET /api/notificacao/{tipoDestinatario}/{destinatarioId}/nao-lidas](#get-apinotificacaotipodestinatariodestinatarioidnao-lidas) - Não lidas
- [GET /api/notificacao/{tipoDestinatario}/{destinatarioId}/count-nao-lidas](#get-apinotificacaotipodestinatariodestinatarioidcount-nao-lidas) - Contar não lidas
- [GET /api/notificacao/{id}](#get-apinotificacao-id) - Buscar por ID
- [PUT /api/notificacao/{id}/marcar-lida](#put-apinotificacao-idmarcar-lida) - Marcar como lida
- [PUT /api/notificacao/{tipoDestinatario}/{destinatarioId}/marcar-todas-lidas](#put-apinotificacaotipodestinatariodestinatarioidmarcar-todas-lidas) - Marcar todas
- [DELETE /api/notificacao/{id}](#delete-apinotificacao-id) - Deletar notificação
- [DELETE /api/notificacao/{tipoDestinatario}/{destinatarioId}/lidas](#delete-apinotificacaotipodestinatariodestinatarioidlidas) - Deletar lidas
- [GET /api/notificacao/{tipoDestinatario}/{destinatarioId}/tipo/{tipo}](#get-apinotificacaotipodestinatariodestinatarioidtipotipo) - Filtrar por tipo
- [GET /api/notificacao/{tipoDestinatario}/{destinatarioId}/recentes](#get-apinotificacaotipodestinatariodestinatarioidrecentes) - Recentes

---

## 📖 MODELOS DE DADOS

### 🏦 Credor
```json
{
  "id": "Long (auto-gerado)",
  "nome": "String",
  "cpf": "String (único, obrigatório)",
  "telefone": "String (obrigatório)",
  "email": "String (único, obrigatório)",
  "senhaHash": "String (opcional)",
  "saldoDisponivel": "Double (padrão: 0.0)"
}
```

### 👤 Devedor
```json
{
  "id": "Long (auto-gerado)",
  "nome": "String (obrigatório)",
  "cpf": "String (único, obrigatório)",
  "telefone": "String (obrigatório)",
  "email": "String (obrigatório)",
  "senhaHash": "String (será hasheado automaticamente)",
  "endereco": "String",
  "cidade": "String",
  "estado": "String",
  "cep": "String",
  "dataNascimento": "LocalDate (formato: YYYY-MM-DD)",
  "credor": {
    "id": "Long (ID do credor associado)"
  }
}
```

### 💰 OfertaEmprestimo
```json
{
  "id": "Long (auto-gerado)",
  "credor": {
    "id": "Long (obrigatório)"
  },
  "valorDisponivel": "Double (obrigatório, > 0)",
  "parcelasMinimas": "Integer (obrigatório, >= 1)",
  "parcelasMaximas": "Integer (obrigatório, >= parcelasMinimas)",
  "diasAtePrimeiraCobranca": "Integer (obrigatório, >= 0)",
  "taxaJuros": "Double (obrigatório, >= 0, percentual ex: 2.5)",
  "dataCriacao": "LocalDateTime (auto-gerado)",
  "ativa": "Boolean (padrão: true)"
}
```

### 📢 PropostaEmprestimo
```json
{
  "id": "Long (auto-gerado)",
  "idPublico": "String (auto-gerado, formato: #ABC123)",
  "ofertaOrigem": {
    "id": "Long"
  },
  "credor": {
    "id": "Long"
  },
  "nomeCredor": "String (desnormalizado)",
  "valorDisponivel": "Double",
  "parcelasMinimas": "Integer",
  "parcelasMaximas": "Integer",
  "diasAtePrimeiraCobranca": "Integer",
  "taxaJuros": "Double",
  "dataCriacao": "LocalDateTime (auto-gerado)",
  "status": "String (ATIVA, CANCELADA, ACEITA)"
}
```

### ❤️ InteresseProposta
```json
{
  "id": "Long (auto-gerado)",
  "proposta": {
    "id": "Long (obrigatório)"
  },
  "devedor": {
    "id": "Long (obrigatório)"
  },
  "dataInteresse": "LocalDateTime (auto-gerado)",
  "status": "String (PENDENTE, APROVADO, REJEITADO, CANCELADO)",
  "mensagem": "String (opcional, max 500 chars)",
  "confirmacaoCredor": "Boolean (padrão: false)",
  "confirmacaoDevedor": "Boolean (padrão: false)",
  "dataConfirmacaoCredor": "LocalDateTime",
  "dataConfirmacaoDevedor": "LocalDateTime"
}
```

### 💳 Emprestimo
```json
{
  "id": "Long (auto-gerado)",
  "devedor": {
    "id": "Long (obrigatório)"
  },
  "credor": {
    "id": "Long (obrigatório)"
  },
  "propostaOrigem": {
    "id": "Long"
  },
  "interesseOrigem": {
    "id": "Long"
  },
  "valorPrincipal": "Double (obrigatório)",
  "jurosAplicados": "Double (obrigatório)",
  "valorTotal": "Double (obrigatório)",
  "numeroParcelas": "Integer (obrigatório)",
  "parcelasPagas": "Integer (padrão: 0)",
  "dataInicio": "LocalDate (padrão: hoje)",
  "dataVencimento": "LocalDate (obrigatório)",
  "status": "String (EM_ANDAMENTO, PAGO, ATRASADO)"
}
```

### 📅 Parcela
```json
{
  "id": "Long (auto-gerado)",
  "emprestimo": {
    "id": "Long (obrigatório)"
  },
  "numeroParcela": "Integer (obrigatório)",
  "valor": "Double (obrigatório)",
  "dataVencimento": "LocalDate (obrigatório)",
  "dataPagamento": "LocalDate",
  "paga": "Boolean (padrão: false)",
  "atrasada": "Boolean (padrão: false)"
}
```

### 🔔 Notificacao
```json
{
  "id": "Long (auto-gerado)",
  "tipoDestinatario": "String (CREDOR ou DEVEDOR)",
  "destinatarioId": "Long (obrigatório)",
  "tipo": "String (NOVO_INTERESSE, APROVACAO, CONFIRMACAO, VENCIMENTO, ATRASO)",
  "titulo": "String (obrigatório, max 200 chars)",
  "mensagem": "String (obrigatório, max 1000 chars)",
  "lida": "Boolean (padrão: false)",
  "dataCriacao": "LocalDateTime (auto-gerado)",
  "dataLeitura": "LocalDateTime",
  "referencia": "String (max 100 chars)",
  "tipoReferencia": "String (max 50 chars)"
}
```

---

## 🔍 DETALHAMENTO DAS ROTAS

## 👤 CREDOR

### POST /api/credor
Cria um novo credor.

**Request Body:**
```json
{
  "nome": "João Silva",
  "cpf": "12345678900",
  "telefone": "(11) 98765-4321",
  "email": "joao@example.com",
  "senhaHash": "senha123",
  "saldoDisponivel": 10000.00
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "12345678900",
  "telefone": "(11) 98765-4321",
  "email": "joao@example.com",
  "senhaHash": "senha123",
  "saldoDisponivel": 10000.00
}
```

---

### POST /api/credor/criar-conta
Cria conta com validação de email único e inicialização de saldo.

**Request Body:**
```json
{
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "telefone": "(11) 91234-5678",
  "email": "maria@example.com",
  "senhaHash": "senha456"
}
```

**Response (201 Created):**
```json
{
  "id": 2,
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "telefone": "(11) 91234-5678",
  "email": "maria@example.com",
  "senhaHash": "senha456",
  "saldoDisponivel": 0.0
}
```

**Validações:**
- Email é obrigatório
- Email deve ser único
- Saldo inicializado em 0.0 se não fornecido

---

### GET /api/credor
Lista todos os credores.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "cpf": "12345678900",
    "telefone": "(11) 98765-4321",
    "email": "joao@example.com",
    "saldoDisponivel": 10000.00
  },
  {
    "id": 2,
    "nome": "Maria Santos",
    "cpf": "98765432100",
    "telefone": "(11) 91234-5678",
    "email": "maria@example.com",
    "saldoDisponivel": 0.0
  }
]
```

---

### GET /api/credor/{id}
Busca credor por ID.

**Response (200 OK):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "12345678900",
  "telefone": "(11) 98765-4321",
  "email": "joao@example.com",
  "saldoDisponivel": 10000.00
}
```

**Response (404 Not Found):**
```json
{
  "erro": "Credor não encontrado"
}
```

---

### PUT /api/credor/{id}
Atualiza dados do credor.

**Request Body:**
```json
{
  "nome": "João Silva Atualizado",
  "cpf": "12345678900",
  "telefone": "(11) 98765-9999",
  "email": "joao.novo@example.com",
  "saldoDisponivel": 15000.00
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "nome": "João Silva Atualizado",
  "cpf": "12345678900",
  "telefone": "(11) 98765-9999",
  "email": "joao.novo@example.com",
  "saldoDisponivel": 15000.00
}
```

---

### DELETE /api/credor/{id}
Deleta um credor.

**Response (204 No Content)**

---

### PUT /api/credor/saldo/{id}
Registra depósito de saldo (soma ao saldo atual).

**Request Body:**
```json
{
  "valor": 5000.00
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "12345678900",
  "telefone": "(11) 98765-4321",
  "email": "joao@example.com",
  "saldoDisponivel": 15000.00
}
```

**Validações:**
- Valor deve ser maior que 0
- Valor é somado ao saldo atual

---

## 👥 DEVEDOR

### POST /api/devedor
Cria um novo devedor.

**Request Body:**
```json
{
  "nome": "Carlos Oliveira",
  "cpf": "11122233344",
  "telefone": "(11) 99999-8888",
  "email": "carlos@example.com",
  "senhaHash": "senha789",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567",
  "dataNascimento": "1990-05-15",
  "credor": {
    "id": 1
  }
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "nome": "Carlos Oliveira",
  "cpf": "11122233344",
  "telefone": "(11) 99999-8888",
  "email": "carlos@example.com",
  "senhaHash": "$2a$10$hashedpassword...",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567",
  "dataNascimento": "1990-05-15"
}
```

**Observações:**
- Senha é automaticamente hasheada
- Credor é opcional mas se fornecido deve existir
- Data de nascimento no formato ISO: YYYY-MM-DD

---

### GET /api/devedor
Lista todos os devedores.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "nome": "Carlos Oliveira",
    "cpf": "11122233344",
    "telefone": "(11) 99999-8888",
    "email": "carlos@example.com",
    "endereco": "Rua das Flores, 123",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01234-567",
    "dataNascimento": "1990-05-15"
  }
]
```

---

### GET /api/devedor/{id}
Busca devedor por ID.

**Response (200 OK):**
```json
{
  "id": 1,
  "nome": "Carlos Oliveira",
  "cpf": "11122233344",
  "telefone": "(11) 99999-8888",
  "email": "carlos@example.com",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567",
  "dataNascimento": "1990-05-15"
}
```

---

## 💰 OFERTA DE EMPRÉSTIMO

### POST /api/oferta
Cria uma nova oferta de empréstimo (privada, apenas o credor vê).

**Request Body:**
```json
{
  "credor": {
    "id": 1
  },
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 3,
  "parcelasMaximas": 12,
  "diasAtePrimeiraCobranca": 30,
  "taxaJuros": 2.5
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 3,
  "parcelasMaximas": 12,
  "diasAtePrimeiraCobranca": 30,
  "taxaJuros": 2.5,
  "dataCriacao": "2024-01-15T10:30:00",
  "ativa": true
}
```

**Validações:**
- Credor é obrigatório e deve existir
- valorDisponivel > 0
- parcelasMinimas >= 1
- parcelasMaximas >= parcelasMinimas
- taxaJuros >= 0
- diasAtePrimeiraCobranca >= 0

---

### GET /api/oferta/credor/{credorId}
Lista todas as ofertas de um credor.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "valorDisponivel": 5000.00,
    "parcelasMinimas": 3,
    "parcelasMaximas": 12,
    "diasAtePrimeiraCobranca": 30,
    "taxaJuros": 2.5,
    "dataCriacao": "2024-01-15T10:30:00",
    "ativa": true
  }
]
```

---

### GET /api/oferta/credor/{credorId}/ativas
Lista apenas ofertas ativas de um credor.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "valorDisponivel": 5000.00,
    "parcelasMinimas": 3,
    "parcelasMaximas": 12,
    "diasAtePrimeiraCobranca": 30,
    "taxaJuros": 2.5,
    "dataCriacao": "2024-01-15T10:30:00",
    "ativa": true
  }
]
```

---

### GET /api/oferta/{id}
Busca oferta por ID.

**Response (200 OK):**
```json
{
  "id": 1,
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 3,
  "parcelasMaximas": 12,
  "diasAtePrimeiraCobranca": 30,
  "taxaJuros": 2.5,
  "dataCriacao": "2024-01-15T10:30:00",
  "ativa": true
}
```

---

### DELETE /api/oferta/{id}
Deleta uma oferta (apenas se não tiver propostas criadas).

**Response (204 No Content)**

**Response (400 Bad Request):**
```json
{
  "erro": "Não é possível deletar oferta que já possui propostas criadas"
}
```

---

### POST /api/oferta/{id}/criar-proposta
Cria uma proposta pública a partir da oferta.

**Response (201 Created):**
```json
{
  "id": 1,
  "idPublico": "#ABC123",
  "nomeCredor": "João Silva",
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 3,
  "parcelasMaximas": 12,
  "diasAtePrimeiraCobranca": 30,
  "taxaJuros": 2.5,
  "dataCriacao": "2024-01-15T10:35:00",
  "status": "ATIVA"
}
```

**Validações:**
- Oferta deve existir
- Oferta deve estar ativa
- ID público é gerado automaticamente

---

### GET /api/oferta/{id}/opcoes-parcelas
Calcula opções de parcelamento para a oferta.

**Response (200 OK):**
```json
[
  {
    "numeroParcelas": 3,
    "valorParcela": 1708.33,
    "valorTotal": 5125.00,
    "jurosTotal": 125.00
  },
  {
    "numeroParcelas": 6,
    "valorParcela": 875.00,
    "valorTotal": 5250.00,
    "jurosTotal": 250.00
  },
  {
    "numeroParcelas": 12,
    "valorParcela": 458.33,
    "valorTotal": 5500.00,
    "jurosTotal": 500.00
  }
]
```

---

## 📢 PROPOSTA DE EMPRÉSTIMO

### GET /api/proposta/publicas
Lista todas as propostas públicas ativas (visíveis para devedores).

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "idPublico": "#ABC123",
    "nomeCredor": "João Silva",
    "valorDisponivel": 5000.00,
    "parcelasMinimas": 3,
    "parcelasMaximas": 12,
    "diasAtePrimeiraCobranca": 30,
    "taxaJuros": 2.5,
    "dataCriacao": "2024-01-15T10:35:00",
    "status": "ATIVA"
  }
]
```

---

### GET /api/proposta/credor/{credorId}
Lista propostas de um credor específico.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "idPublico": "#ABC123",
    "nomeCredor": "João Silva",
    "valorDisponivel": 5000.00,
    "parcelasMinimas": 3,
    "parcelasMaximas": 12,
    "diasAtePrimeiraCobranca": 30,
    "taxaJuros": 2.5,
    "dataCriacao": "2024-01-15T10:35:00",
    "status": "ATIVA"
  }
]
```

---

### GET /api/proposta/publico/{idPublico}
Busca proposta pelo ID público (ex: #ABC123).

**Response (200 OK):**
```json
{
  "id": 1,
  "idPublico": "#ABC123",
  "nomeCredor": "João Silva",
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 3,
  "parcelasMaximas": 12,
  "diasAtePrimeiraCobranca": 30,
  "taxaJuros": 2.5,
  "dataCriacao": "2024-01-15T10:35:00",
  "status": "ATIVA"
}
```

---

### GET /api/proposta/{id}
Busca proposta por ID numérico.

**Response (200 OK):**
```json
{
  "id": 1,
  "idPublico": "#ABC123",
  "nomeCredor": "João Silva",
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 3,
  "parcelasMaximas": 12,
  "diasAtePrimeiraCobranca": 30,
  "taxaJuros": 2.5,
  "dataCriacao": "2024-01-15T10:35:00",
  "status": "ATIVA"
}
```

---

### PUT /api/proposta/{id}/cancelar
Cancela uma proposta (apenas se não tiver interesses pendentes).

**Response (200 OK):**
```json
{
  "id": 1,
  "idPublico": "#ABC123",
  "nomeCredor": "João Silva",
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 3,
  "parcelasMaximas": 12,
  "diasAtePrimeiraCobranca": 30,
  "taxaJuros": 2.5,
  "dataCriacao": "2024-01-15T10:35:00",
  "status": "CANCELADA"
}
```

**Validações:**
- Proposta não pode estar já cancelada
- Proposta não pode estar aceita
- Não pode ter interesses pendentes

---

### GET /api/proposta/{id}/detalhes
Obtém detalhes completos da proposta com cálculos.

**Response (200 OK):**
```json
{
  "proposta": {
    "id": 1,
    "idPublico": "#ABC123",
    "nomeCredor": "João Silva",
    "valorDisponivel": 5000.00,
    "parcelasMinimas": 3,
    "parcelasMaximas": 12,
    "diasAtePrimeiraCobranca": 30,
    "taxaJuros": 2.5,
    "status": "ATIVA"
  },
  "opcoesParcelas": [
    {
      "numeroParcelas": 3,
      "valorParcela": 1708.33,
      "valorTotal": 5125.00,
      "jurosTotal": 125.00
    }
  ],
  "dataPrimeiraParcela": "2024-02-14",
  "totalInteresses": 2
}
```

---

### GET /api/proposta/status/{status}
Filtra propostas por status (ATIVA, CANCELADA, ACEITA).

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "idPublico": "#ABC123",
    "nomeCredor": "João Silva",
    "valorDisponivel": 5000.00,
    "status": "ATIVA"
  }
]
```

---

### GET /api/proposta/buscar?valorMin={min}&valorMax={max}
Busca propostas por faixa de valor.

**Exemplo:** `/api/proposta/buscar?valorMin=1000&valorMax=10000`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "idPublico": "#ABC123",
    "nomeCredor": "João Silva",
    "valorDisponivel": 5000.00,
    "status": "ATIVA"
  }
]
```

---

### GET /api/proposta/{id}/estatisticas
Obtém estatísticas de uma proposta.

**Response (200 OK):**
```json
{
  "totalInteresses": 5,
  "interessesPendentes": 2,
  "interessesAprovados": 2,
  "emprestimosGerados": 1,
  "status": "ATIVA"
}
```

---

## ❤️ INTERESSE EM PROPOSTA

### POST /api/interesse
Devedor demonstra interesse em uma proposta.

**Request Body:**
```json
{
  "proposta": {
    "id": 1
  },
  "devedor": {
    "id": 1
  },
  "mensagem": "Tenho interesse nesta proposta"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "dataInteresse": "2024-01-15T11:00:00",
  "status": "PENDENTE",
  "mensagem": "Tenho interesse nesta proposta",
  "confirmacaoCredor": false,
  "confirmacaoDevedor": false
}
```

**Validações:**
- Proposta deve existir e estar ativa
- Devedor deve existir
- Devedor deve ter dados completos (endereço, cidade, estado, CEP)
- Devedor não pode ter interesse duplicado na mesma proposta

---

### GET /api/interesse/proposta/{propostaId}
Lista todos os interessados em uma proposta (para o Credor).

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "dataInteresse": "2024-01-15T11:00:00",
    "status": "PENDENTE",
    "mensagem": "Tenho interesse nesta proposta",
    "confirmacaoCredor": false,
    "confirmacaoDevedor": false
  }
]
```

---

### GET /api/interesse/devedor/{devedorId}
Lista meus interesses (para o Devedor).

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "dataInteresse": "2024-01-15T11:00:00",
    "status": "PENDENTE",
    "mensagem": "Tenho interesse nesta proposta",
    "confirmacaoCredor": false,
    "confirmacaoDevedor": false
  }
]
```

---

### PUT /api/interesse/{id}/aprovar
Credor aprova o interesse.

**Response (200 OK):**
```json
{
  "id": 1,
  "dataInteresse": "2024-01-15T11:00:00",
  "status": "APROVADO",
  "mensagem": "Tenho interesse nesta proposta",
  "confirmacaoCredor": false,
  "confirmacaoDevedor": false
}
```

**Validações:**
- Interesse deve estar PENDENTE

---

### PUT /api/interesse/{id}/rejeitar
Credor rejeita o interesse.

**Response (200 OK):**
```json
{
  "id": 1,
  "dataInteresse": "2024-01-15T11:00:00",
  "status": "REJEITADO",
  "mensagem": "Tenho interesse nesta proposta",
  "confirmacaoCredor": false,
  "confirmacaoDevedor": false
}
```

**Validações:**
- Interesse deve estar PENDENTE ou APROVADO

---

### POST /api/interesse/{id}/confirmar-credor
Credor confirma o empréstimo (escolhe número de parcelas).

**Request Body:**
```json
{
  "numeroParcelas": 6
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "dataInteresse": "2024-01-15T11:00:00",
  "status": "APROVADO",
  "confirmacaoCredor": true,
  "confirmacaoDevedor": false,
  "dataConfirmacaoCredor": "2024-01-15T14:00:00"
}
```

**Validações:**
- Interesse deve estar APROVADO
- Credor não pode ter confirmado antes
- numeroParcelas deve estar entre parcelasMinimas e parcelasMaximas
- Credor deve ter saldo suficiente
- Se ambos confirmaram, cria o empréstimo automaticamente

---

### POST /api/interesse/{id}/confirmar-devedor
Devedor confirma o empréstimo (escolhe número de parcelas).

**Request Body:**
```json
{
  "numeroParcelas": 6
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "dataInteresse": "2024-01-15T11:00:00",
  "status": "APROVADO",
  "confirmacaoCredor": false,
  "confirmacaoDevedor": true,
  "dataConfirmacaoDevedor": "2024-01-15T15:00:00"
}
```

**Validações:**
- Interesse deve estar APROVADO
- Devedor não pode ter confirmado antes
- numeroParcelas deve estar entre parcelasMinimas e parcelasMaximas
- Se ambos confirmaram, cria o empréstimo automaticamente

---

### DELETE /api/interesse/{id}
Devedor cancela o interesse.

**Response (204 No Content)**

**Validações:**
- Não pode cancelar se já houver confirmações

---

## 💳 EMPRÉSTIMO

### GET /api/emprestimo
Lista todos os empréstimos.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "valorPrincipal": 5000.00,
    "jurosAplicados": 250.00,
    "valorTotal": 5250.00,
    "numeroParcelas": 6,
    "parcelasPagas": 2,
    "dataInicio": "2024-01-15",
    "dataVencimento": "2024-07-15",
    "status": "EM_ANDAMENTO"
  }
]
```

---

### GET /api/emprestimo/{id}
Busca empréstimo por ID.

**Response (200 OK):**
```json
{
  "id": 1,
  "valorPrincipal": 5000.00,
  "jurosAplicados": 250.00,
  "valorTotal": 5250.00,
  "numeroParcelas": 6,
  "parcelasPagas": 2,
  "dataInicio": "2024-01-15",
  "dataVencimento": "2024-07-15",
  "status": "EM_ANDAMENTO"
}
```

---

### POST /api/emprestimo
Cria um novo empréstimo manualmente.

**Request Body:**
```json
{
  "credor": {
    "id": 1
  },
  "devedor": {
    "id": 1
  },
  "valorPrincipal": 5000.00,
  "jurosAplicados": 250.00,
  "valorTotal": 5250.00,
  "numeroParcelas": 6,
  "dataInicio": "2024-01-15",
  "dataVencimento": "2024-07-15",
  "status": "EM_ANDAMENTO"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "valorPrincipal": 5000.00,
  "jurosAplicados": 250.00,
  "valorTotal": 5250.00,
  "numeroParcelas": 6,
  "parcelasPagas": 0,
  "dataInicio": "2024-01-15",
  "dataVencimento": "2024-07-15",
  "status": "EM_ANDAMENTO"
}
```

---

### PUT /api/emprestimo/{id}
Atualiza um empréstimo.

**Request Body:**
```json
{
  "valorPrincipal": 5000.00,
  "jurosAplicados": 250.00,
  "valorTotal": 5250.00,
  "dataInicio": "2024-01-15",
  "dataVencimento": "2024-07-15",
  "status": "EM_ANDAMENTO"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "valorPrincipal": 5000.00,
  "jurosAplicados": 250.00,
  "valorTotal": 5250.00,
  "numeroParcelas": 6,
  "parcelasPagas": 2,
  "dataInicio": "2024-01-15",
  "dataVencimento": "2024-07-15",
  "status": "EM_ANDAMENTO"
}
```

---

### DELETE /api/emprestimo/{id}
Deleta um empréstimo.

**Response (204 No Content)**

---

## 📅 PARCELA

### GET /api/parcela/emprestimo/{emprestimoId}
Lista todas as parcelas de um empréstimo.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "numeroParcela": 1,
    "valor": 875.00,
    "dataVencimento": "2024-02-14",
    "dataPagamento": "2024-02-14",
    "paga": true,
    "atrasada": false
  },
  {
    "id": 2,
    "numeroParcela": 2,
    "valor": 875.00,
    "dataVencimento": "2024-03-14",
    "dataPagamento": null,
    "paga": false,
    "atrasada": false
  }
]
```

---

### GET /api/parcela/{id}
Busca parcela por ID.

**Response (200 OK):**
```json
{
  "id": 1,
  "numeroParcela": 1,
  "valor": 875.00,
  "dataVencimento": "2024-02-14",
  "dataPagamento": "2024-02-14",
  "paga": true,
  "atrasada": false
}
```

---

### PUT /api/parcela/{id}/pagar
Marca parcela como paga (apenas Credor).

**Response (200 OK):**
```json
{
  "id": 2,
  "numeroParcela": 2,
  "valor": 875.00,
  "dataVencimento": "2024-03-14",
  "dataPagamento": "2024-03-14",
  "paga": true,
  "atrasada": false
}
```

**Validações:**
- Parcela não pode estar já paga
- Atualiza contador de parcelas pagas do empréstimo
- Atualiza status do empréstimo se necessário
- Envia notificações

---

### GET /api/parcela/emprestimo/{emprestimoId}/pendentes
Lista parcelas pendentes de um empréstimo.

**Response (200 OK):**
```json
[
  {
    "id": 2,
    "numeroParcela": 2,
    "valor": 875.00,
    "dataVencimento": "2024-03-14",
    "paga": false,
    "atrasada": false
  }
]
```

---

### GET /api/parcela/emprestimo/{emprestimoId}/pagas
Lista parcelas pagas de um empréstimo.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "numeroParcela": 1,
    "valor": 875.00,
    "dataVencimento": "2024-02-14",
    "dataPagamento": "2024-02-14",
    "paga": true,
    "atrasada": false
  }
]
```

---

### GET /api/parcela/emprestimo/{emprestimoId}/atrasadas
Lista parcelas atrasadas de um empréstimo.

**Response (200 OK):**
```json
[
  {
    "id": 3,
    "numeroParcela": 3,
    "valor": 875.00,
    "dataVencimento": "2024-01-14",
    "paga": false,
    "atrasada": true
  }
]
```

---

### GET /api/parcela/emprestimo/{emprestimoId}/resumo
Obtém resumo completo das parcelas.

**Response (200 OK):**
```json
{
  "emprestimoId": 1,
  "valorTotal": 5250.00,
  "totalPago": 1750.00,
  "totalPendente": 3500.00,
  "numeroParcelas": 6,
  "parcelasPagas": 2,
  "parcelasPendentes": 4,
  "parcelasAtrasadas": 1,
  "status": "ATRASADO",
  "proximaParcela": {
    "id": 3,
    "numeroParcela": 3,
    "valor": 875.00,
    "dataVencimento": "2024-04-14"
  },
  "percentualPago": 33.33
}
```

---

### GET /api/parcela/vencidas
Lista todas as parcelas vencidas do sistema.

**Response (200 OK):**
```json
[
  {
    "id": 3,
    "numeroParcela": 3,
    "valor": 875.00,
    "dataVencimento": "2024-01-14",
    "paga": false,
    "atrasada": true
  }
]
```

---

### GET /api/parcela/emprestimo/{emprestimoId}/proximas-vencimento
Lista parcelas próximas do vencimento (próximos 7 dias).

**Response (200 OK):**
```json
[
  {
    "id": 4,
    "numeroParcela": 4,
    "valor": 875.00,
    "dataVencimento": "2024-01-20",
    "paga": false,
    "atrasada": false
  }
]
```

---

## 🔔 NOTIFICAÇÃO

### GET /api/notificacao/{tipoDestinatario}/{destinatarioId}
Lista todas as notificações de um usuário.

**Exemplo:** `/api/notificacao/CREDOR/1`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "tipoDestinatario": "CREDOR",
    "destinatarioId": 1,
    "tipo": "NOVO_INTERESSE",
    "titulo": "Novo Interesse",
    "mensagem": "Carlos Oliveira demonstrou interesse na proposta #ABC123",
    "lida": false,
    "dataCriacao": "2024-01-15T11:00:00",
    "dataLeitura": null,
    "referencia": "1",
    "tipoReferencia": "INTERESSE"
  }
]
```

**Validações:**
- tipoDestinatario deve ser CREDOR ou DEVEDOR

---

### GET /api/notificacao/{tipoDestinatario}/{destinatarioId}/nao-lidas
Lista notificações não lidas.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "tipoDestinatario": "CREDOR",
    "destinatarioId": 1,
    "tipo": "NOVO_INTERESSE",
    "titulo": "Novo Interesse",
    "mensagem": "Carlos Oliveira demonstrou interesse na proposta #ABC123",
    "lida": false,
    "dataCriacao": "2024-01-15T11:00:00"
  }
]
```

---

### GET /api/notificacao/{tipoDestinatario}/{destinatarioId}/count-nao-lidas
Conta notificações não lidas.

**Response (200 OK):**
```json
5
```

---

### GET /api/notificacao/{id}
Busca notificação por ID.

**Response (200 OK):**
```json
{
  "id": 1,
  "tipoDestinatario": "CREDOR",
  "destinatarioId": 1,
  "tipo": "NOVO_INTERESSE",
  "titulo": "Novo Interesse",
  "mensagem": "Carlos Oliveira demonstrou interesse na proposta #ABC123",
  "lida": false,
  "dataCriacao": "2024-01-15T11:00:00"
}
```

---

### PUT /api/notificacao/{id}/marcar-lida
Marca notificação como lida.

**Response (200 OK):**
```json
{
  "id": 1,
  "tipoDestinatario": "CREDOR",
  "destinatarioId": 1,
  "tipo": "NOVO_INTERESSE",
  "titulo": "Novo Interesse",
  "mensagem": "Carlos Oliveira demonstrou interesse na proposta #ABC123",
  "lida": true,
  "dataCriacao": "2024-01-15T11:00:00",
  "dataLeitura": "2024-01-15T12:00:00"
}
```

---

### PUT /api/notificacao/{tipoDestinatario}/{destinatarioId}/marcar-todas-lidas
Marca todas as notificações como lidas.

**Response (200 OK):**
```json
{
  "marcadas": 5
}
```

---

### DELETE /api/notificacao/{id}
Deleta uma notificação.

**Response (204 No Content)**

---

### DELETE /api/notificacao/{tipoDestinatario}/{destinatarioId}/lidas
Deleta todas as notificações lidas.

**Response (200 OK):**
```json
{
  "deletadas": 3
}
```

---

### GET /api/notificacao/{tipoDestinatario}/{destinatarioId}/tipo/{tipo}
Filtra notificações por tipo.

**Tipos válidos:**
- NOVO_INTERESSE
- APROVACAO
- CONFIRMACAO
- VENCIMENTO
- ATRASO

**Exemplo:** `/api/notificacao/CREDOR/1/tipo/NOVO_INTERESSE`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "tipo": "NOVO_INTERESSE",
    "titulo": "Novo Interesse",
    "mensagem": "Carlos Oliveira demonstrou interesse na proposta #ABC123",
    "lida": false,
    "dataCriacao": "2024-01-15T11:00:00"
  }
]
```

---

### GET /api/notificacao/{tipoDestinatario}/{destinatarioId}/recentes
Lista notificações das últimas 24 horas.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "tipo": "NOVO_INTERESSE",
    "titulo": "Novo Interesse",
    "mensagem": "Carlos Oliveira demonstrou interesse na proposta #ABC123",
    "lida": false,
    "dataCriacao": "2024-01-15T11:00:00"
  }
]
```

---

## 🔄 FLUXO COMPLETO DO SISTEMA

### 1️⃣ Credor cria conta e adiciona saldo
```
POST /api/credor/criar-conta
PUT /api/credor/saldo/{id}
```

### 2️⃣ Credor cria oferta privada
```
POST /api/oferta
```

### 3️⃣ Credor publica proposta
```
POST /api/oferta/{id}/criar-proposta
```

### 4️⃣ Devedor cria conta
```
POST /api/devedor
```

### 5️⃣ Devedor vê propostas públicas
```
GET /api/proposta/publicas
```

### 6️⃣ Devedor demonstra interesse
```
POST /api/interesse
```

### 7️⃣ Credor recebe notificação e aprova
```
GET /api/notificacao/CREDOR/{id}/nao-lidas
PUT /api/interesse/{id}/aprovar
```

### 8️⃣ Confirmação bilateral
```
POST /api/interesse/{id}/confirmar-credor
POST /api/interesse/{id}/confirmar-devedor
```

### 9️⃣ Empréstimo criado automaticamente
```
GET /api/emprestimo/{id}
GET /api/parcela/emprestimo/{id}
```

### 🔟 Pagamento de parcelas
```
PUT /api/parcela/{id}/pagar
```

---

## ⚠️ CÓDIGOS DE STATUS HTTP

- **200 OK** - Requisição bem-sucedida
- **201 Created** - Recurso criado com sucesso
- **204 No Content** - Operação bem-sucedida sem conteúdo de retorno
- **400 Bad Request** - Dados inválidos ou regra de negócio violada
- **404 Not Found** - Recurso não encontrado
- **409 Conflict** - Conflito (ex: email duplicado)
- **500 Internal Server Error** - Erro interno do servidor

---

## 📝 OBSERVAÇÕES IMPORTANTES

### Formatos de Data
- **LocalDate**: `"YYYY-MM-DD"` (ex: `"2024-01-15"`)
- **LocalDateTime**: `"YYYY-MM-DDTHH:mm:ss"` (ex: `"2024-01-15T10:30:00"`)

### Relacionamentos
- Ao enviar objetos relacionados, envie apenas o ID:
```json
{
  "credor": {
    "id": 1
  }
}
```

### Campos Auto-gerados
Não envie estes campos nas requisições POST:
- `id`
- `dataCriacao`
- `dataInteresse`
- `idPublico` (para propostas)
- Campos com valores padrão

### Validações Comuns
- CPF e Email devem ser únicos
- Valores monetários devem ser > 0
- Datas devem estar no formato ISO
- Status devem usar valores específicos (ATIVA, PENDENTE, etc.)

---

## 🎯 CASOS DE USO PRÁTICOS

### Criar Credor Completo
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/criar-conta \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "cpf": "12345678900",
    "telefone": "(11) 98765-4321",
    "email": "joao@example.com"
  }'
```

### Adicionar Saldo
```bash
curl -X PUT http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/saldo/1 \
  -H "Content-Type: application/json" \
  -d '{
    "valor": 10000.00
  }'
```

### Criar Oferta
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/oferta \
  -H "Content-Type: application/json" \
  -d '{
    "credor": {"id": 1},
    "valorDisponivel": 5000.00,
    "parcelasMinimas": 3,
    "parcelasMaximas": 12,
    "diasAtePrimeiraCobranca": 30,
    "taxaJuros": 2.5
  }'
```

### Publicar Proposta
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/oferta/1/criar-proposta
```

### Demonstrar Interesse
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/interesse \
  -H "Content-Type: application/json" \
  -d '{
    "proposta": {"id": 1},
    "devedor": {"id": 1},
    "mensagem": "Tenho interesse nesta proposta"
  }'
```

---

**Documentação gerada em:** 2024-01-15  
**Versão da API:** 1.0  
**Sistema:** Agilit Loan Management
# 🚀 EXEMPLOS COMPLETOS DE REQUISIÇÕES - API Agilit v2.0

> **Versão 2.0** - Exemplos atualizados com Controllers Baseados em Casos de Uso

## 📌 Base URL

```
http://localhost:8080/Sistemas_Distriubidos_Agilit/api
```

---

## 📑 ÍNDICE

### 🔐 Autenticação
1. [Login Unificado](#1-login-unificado)
2. [Verificar Email](#2-verificar-email)
3. [Verificar CPF](#3-verificar-cpf)

### 👤 Credor - Casos de Uso
4. [UC-C01: Criar Conta](#4-uc-c01-criar-conta-credor)
5. [UC-C02: Fazer Login](#5-uc-c02-fazer-login-credor)
6. [UC-C03: Criar Oferta](#6-uc-c03-criar-oferta-de-empréstimo)
7. [UC-C04: Gerar Proposta](#7-uc-c04-gerar-proposta-de-empréstimo)
8. [UC-C05: Registrar Empréstimo](#8-uc-c05-registrar-empréstimo)

### 👥 Devedor - Casos de Uso
9. [UC-D01: Criar Conta](#9-uc-d01-criar-conta-devedor)
10. [UC-D02: Fazer Login](#10-uc-d02-fazer-login-devedor)
11. [UC-D03: Buscar Propostas](#11-uc-d03-buscar-propostas-de-empréstimo)
12. [UC-D04: Selecionar Proposta](#12-uc-d04-selecionar-proposta)
13. [UC-D05: Pedir Empréstimo](#13-uc-d05-pedir-empréstimo)
14. [UC-D06: Aceitar Termos](#14-uc-d06-aceitar-termos)

### 🔄 Fluxos Completos
15. [Fluxo Completo do Credor](#15-fluxo-completo-do-credor)
16. [Fluxo Completo do Devedor](#16-fluxo-completo-do-devedor)
17. [Fluxo de Criação de Empréstimo](#17-fluxo-de-criação-de-empréstimo)

---

## 🔐 AUTENTICAÇÃO

### 1. Login Unificado

**POST** `/api/auth/login`

Login unificado para Credor ou Devedor.

**Request:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@email.com",
    "senha": "senha123",
    "tipo": "CREDOR"
  }'
```

**Response 200:**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@email.com",
  "tipo": "CREDOR",
  "mensagem": "Login realizado com sucesso"
}
```

**Response 401 (Credenciais inválidas):**
```json
{
  "erro": "Email ou senha inválidos"
}
```

---

### 2. Verificar Email

**GET** `/api/auth/verificar-email?email={email}&tipo={tipo}`

Verifica se um email está disponível para cadastro.

**Request:**
```bash
curl -X GET "http://localhost:8080/Sistemas_Distriubidos_Agilit/api/auth/verificar-email?email=joao@email.com&tipo=CREDOR"
```

**Response 200:**
```json
{
  "disponivel": false,
  "email": "joao@email.com",
  "tipo": "CREDOR"
}
```

---

### 3. Verificar CPF

**GET** `/api/auth/verificar-cpf?cpf={cpf}&tipo={tipo}`

Verifica se um CPF está disponível para cadastro.

**Request:**
```bash
curl -X GET "http://localhost:8080/Sistemas_Distriubidos_Agilit/api/auth/verificar-cpf?cpf=12345678900&tipo=CREDOR"
```

**Response 200:**
```json
{
  "disponivel": true,
  "cpf": "12345678900",
  "tipo": "CREDOR"
}
```

---

## 👤 CREDOR - CASOS DE USO

### 4. UC-C01: Criar Conta (Credor)

**POST** `/api/credor/criar-conta`

Cria uma nova conta de credor com validações completas.

**Request:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/criar-conta \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "cpf": "12345678900",
    "email": "joao@email.com",
    "senhaHash": "senha123",
    "telefone": "(11) 98765-4321",
    "saldoDisponivel": 10000.00
  }'
```

**Response 201:**
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "12345678900",
  "email": "joao@email.com",
  "telefone": "(11) 98765-4321",
  "saldoDisponivel": 10000.00
}
```

**Response 409 (Email já cadastrado):**
```json
{
  "erro": "Email já cadastrado"
}
```

**Response 409 (CPF já cadastrado):**
```json
{
  "erro": "CPF já cadastrado"
}
```

**Validações:**
- ✅ Nome obrigatório
- ✅ Email obrigatório e único
- ✅ Senha obrigatória (hasheada automaticamente)
- ✅ CPF obrigatório e único
- ✅ Saldo inicial padrão: R$ 0,00

---

### 5. UC-C02: Fazer Login (Credor)

**POST** `/api/credor/login`

Autentica um credor no sistema.

**Request:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@email.com",
    "senha": "senha123"
  }'
```

**Response 200:**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@email.com",
  "cpf": "12345678900",
  "telefone": "(11) 98765-4321",
  "saldoDisponivel": 10000.00,
  "tipo": "CREDOR",
  "mensagem": "Login realizado com sucesso"
}
```

---

### 6. UC-C03: Criar Oferta de Empréstimo

**POST** `/api/credor/criar-oferta`

Cria uma oferta privada de empréstimo.

**Request:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/criar-oferta \
  -H "Content-Type: application/json" \
  -d '{
    "credor": { "id": 1 },
    "valorDisponivel": 5000.00,
    "parcelasMinimas": 6,
    "parcelasMaximas": 24,
    "taxaJuros": 2.5,
    "diasAtePrimeiraCobranca": 30
  }'
```

**Response 201:**
```json
{
  "id": 1,
  "credor": {
    "id": 1,
    "nome": "João Silva"
  },
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 6,
  "parcelasMaximas": 24,
  "taxaJuros": 2.5,
  "diasAtePrimeiraCobranca": 30,
  "dataCriacao": "2024-12-03T14:30:00",
  "ativa": true
}
```

**Response 400 (Saldo insuficiente):**
```json
{
  "erro": "Saldo insuficiente. Disponível: R$ 3000.00, Necessário: R$ 5000.00"
}
```

---

#### Listar Minhas Ofertas

**GET** `/api/credor/criar-oferta/minhas/{credorId}`

**Request:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/criar-oferta/minhas/1
```

**Response 200:**
```json
[
  {
    "id": 1,
    "valorDisponivel": 5000.00,
    "parcelasMinimas": 6,
    "parcelasMaximas": 24,
    "taxaJuros": 2.5,
    "ativa": true,
    "dataCriacao": "2024-12-03T14:30:00"
  },
  {
    "id": 2,
    "valorDisponivel": 3000.00,
    "parcelasMinimas": 3,
    "parcelasMaximas": 12,
    "taxaJuros": 3.0,
    "ativa": false,
    "dataCriacao": "2024-12-02T10:15:00"
  }
]
```

---

#### Listar Apenas Ofertas Ativas

**GET** `/api/credor/criar-oferta/minhas/{credorId}/ativas`

**Request:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/criar-oferta/minhas/1/ativas
```

---

### 7. UC-C04: Gerar Proposta de Empréstimo

**POST** `/api/credor/gerar-proposta`

Transforma uma oferta privada em proposta pública.

**Request:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/gerar-proposta \
  -H "Content-Type: application/json" \
  -d '{
    "ofertaId": 1,
    "taxaJuros": 2.5
  }'
```

**Response 201:**
```json
{
  "id": 1,
  "idPublico": "PROP-ABC123",
  "ofertaOrigem": {
    "id": 1
  },
  "credor": {
    "id": 1,
    "nome": "João Silva"
  },
  "nomeCredor": "João Silva",
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 6,
  "parcelasMaximas": 24,
  "taxaJuros": 2.5,
  "diasAtePrimeiraCobranca": 30,
  "dataCriacao": "2024-12-03T14:35:00",
  "status": "ATIVA"
}
```

**Observação:** O campo `taxaJuros` é opcional. Se não fornecido, usa a taxa da oferta original.

---

#### Listar Minhas Propostas

**GET** `/api/credor/gerar-proposta/minhas/{credorId}`

**Request:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/gerar-proposta/minhas/1
```

**Response 200:**
```json
[
  {
    "id": 1,
    "idPublico": "PROP-ABC123",
    "valorDisponivel": 5000.00,
    "taxaJuros": 2.5,
    "status": "ATIVA",
    "dataCriacao": "2024-12-03T14:35:00"
  }
]
```

---

#### Cancelar Proposta

**PUT** `/api/credor/gerar-proposta/{propostaId}/cancelar`

**Request:**
```bash
curl -X PUT http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/gerar-proposta/1/cancelar
```

**Response 200:**
```json
{
  "id": 1,
  "idPublico": "PROP-ABC123",
  "status": "CANCELADA"
}
```

**Response 400 (Tem interesses pendentes):**
```json
{
  "erro": "Não é possível cancelar proposta com interesses pendentes. Rejeite os interesses primeiro."
}
```

---

### 8. UC-C05: Registrar Empréstimo

**POST** `/api/credor/registrar-emprestimo/{interesseId}/confirmar`

Confirma a criação do empréstimo pelo credor.

**Request:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/registrar-emprestimo/1/confirmar \
  -H "Content-Type: application/json" \
  -d '{
    "numeroParcelas": 12
  }'
```

**Response 200 (Aguardando devedor):**
```json
{
  "mensagem": "Confirmação registrada. Aguardando confirmação do devedor.",
  "interesse": {
    "id": 1,
    "status": "APROVADO",
    "confirmacaoCredor": true,
    "confirmacaoDevedor": false,
    "dataConfirmacaoCredor": "2024-12-03T15:00:00"
  },
  "emprestimo": null
}
```

**Response 200 (Empréstimo criado - ambos confirmaram):**
```json
{
  "mensagem": "Empréstimo criado com sucesso",
  "interesse": {
    "id": 1,
    "status": "APROVADO",
    "confirmacaoCredor": true,
    "confirmacaoDevedor": true
  },
  "emprestimo": {
    "id": 1,
    "valorPrincipal": 5000.00,
    "jurosAplicados": 150.00,
    "valorTotal": 5150.00,
    "numeroParcelas": 12,
    "parcelasPagas": 0,
    "dataInicio": "2024-12-03",
    "dataVencimento": "2025-12-03",
    "status": "EM_ANDAMENTO"
  }
}
```

---

#### Listar Interesses Pendentes de Confirmação

**GET** `/api/credor/registrar-emprestimo/pendentes/{credorId}`

**Request:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/registrar-emprestimo/pendentes/1
```

**Response 200:**
```json
[
  {
    "id": 1,
    "proposta": {
      "idPublico": "PROP-ABC123"
    },
    "devedor": {
      "nome": "Maria Santos"
    },
    "status": "APROVADO",
    "confirmacaoCredor": false,
    "confirmacaoDevedor": false
  }
]
```

---

#### Listar Meus Empréstimos

**GET** `/api/credor/registrar-emprestimo/meus/{credorId}`

**Request:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/registrar-emprestimo/meus/1
```

**Response 200:**
```json
[
  {
    "id": 1,
    "devedor": {
      "nome": "Maria Santos"
    },
    "valorPrincipal": 5000.00,
    "valorTotal": 5150.00,
    "numeroParcelas": 12,
    "parcelasPagas": 2,
    "status": "EM_ANDAMENTO",
    "dataInicio": "2024-12-03"
  }
]
```

---

## 👥 DEVEDOR - CASOS DE USO

### 9. UC-D01: Criar Conta (Devedor)

**POST** `/api/devedor/criar-conta`

Cria uma nova conta de devedor.

**Request:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/criar-conta \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Santos",
    "cpf": "98765432100",
    "email": "maria@email.com",
    "senhaHash": "senha123",
    "telefone": "(11) 91234-5678",
    "endereco": "Rua das Flores, 123",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01234-567"
  }'
```

**Response 201:**
```json
{
  "id": 1,
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "email": "maria@email.com",
  "telefone": "(11) 91234-5678",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567"
}
```

**Observação:** Dados de endereço são opcionais no cadastro inicial, mas serão obrigatórios para demonstrar interesse em propostas.

---

#### Completar Dados Cadastrais

**PUT** `/api/devedor/criar-conta/{devedorId}/completar-dados`

**Request:**
```bash
curl -X PUT http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/criar-conta/1/completar-dados \
  -H "Content-Type: application/json" \
  -d '{
    "endereco": "Rua das Flores, 123",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01234-567",
    "telefone": "(11) 91234-5678"
  }'
```

**Response 200:**
```json
{
  "id": 1,
  "nome": "Maria Santos",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567"
}
```

---

### 10. UC-D02: Fazer Login (Devedor)

**POST** `/api/devedor/login`

Autentica um devedor no sistema.

**Request:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "maria@email.com",
    "senha": "senha123"
  }'
```

**Response 200 (Dados completos):**
```json
{
  "id": 1,
  "nome": "Maria Santos",
  "email": "maria@email.com",
  "cpf": "98765432100",
  "telefone": "(11) 91234-5678",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567",
  "dadosCompletos": true,
  "tipo": "DEVEDOR",
  "mensagem": "Login realizado com sucesso"
}
```

**Response 200 (Dados incompletos):**
```json
{
  "id": 1,
  "nome": "Maria Santos",
  "email": "maria@email.com",
  "dadosCompletos": false,
  "tipo": "DEVEDOR",
  "mensagem": "Login realizado com sucesso",
  "aviso": "Complete seus dados cadastrais para poder demonstrar interesse em propostas"
}
```

---

### 11. UC-D03: Buscar Propostas de Empréstimo

**GET** `/api/devedor/buscar-propostas`

Lista propostas públicas com filtros opcionais.

**Request (sem filtros):**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/buscar-propostas
```

**Request (com filtros):**
```bash
curl -X GET "http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/buscar-propostas?valorMin=1000&valorMax=5000&taxaJurosMax=3.0"
```

**Response 200:**
```json
[
  {
    "id": 1,
    "idPublico": "PROP-ABC123",
    "nomeCredor": "João Silva",
    "valorDisponivel": 5000.00,
    "parcelasMinimas": 6,
    "parcelasMaximas": 24,
    "taxaJuros": 2.5,
    "diasAtePrimeiraCobranca": 30,
    "status": "ATIVA",
    "dataCriacao": "2024-12-03T14:35:00"
  },
  {
    "id": 2,
    "idPublico": "PROP-XYZ789",
    "nomeCredor": "Pedro Oliveira",
    "valorDisponivel": 3000.00,
    "parcelasMinimas": 3,
    "parcelasMaximas": 12,
    "taxaJuros": 2.8,
    "diasAtePrimeiraCobranca": 15,
    "status": "ATIVA",
    "dataCriacao": "2024-12-03T10:20:00"
  }
]
```

---

#### Buscar Proposta Específica

**GET** `/api/devedor/buscar-propostas/{idPublico}`

**Request:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/buscar-propostas/PROP-ABC123
```

**Response 200:**
```json
{
  "id": 1,
  "idPublico": "PROP-ABC123",
  "nomeCredor": "João Silva",
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 6,
  "parcelasMaximas": 24,
  "taxaJuros": 2.5,
  "status": "ATIVA"
}
```

---

#### Detalhes Completos com Simulações

**GET** `/api/devedor/buscar-propostas/{idPublico}/detalhes`

**Request:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/buscar-propostas/PROP-ABC123/detalhes
```

**Response 200:**
```json
{
  "proposta": {
    "id": 1,
    "idPublico": "PROP-ABC123",
    "valorDisponivel": 5000.00,
    "parcelasMinimas": 6,
    "parcelasMaximas": 24,
    "taxaJuros": 2.5,
    "diasAtePrimeiraCobranca": 30
  },
  "opcoesParcelas": [
    {
      "numeroParcelas": 6,
      "valorParcela": 875.00,
      "valorTotal": 5250.00,
      "jurosTotal": 250.00
    },
    {
      "numeroParcelas": 12,
      "valorParcela": 429.17,
      "valorTotal": 5150.00,
      "jurosTotal": 150.00
    },
    {
      "numeroParcelas": 24,
      "valorParcela": 212.50,
      "valorTotal": 5100.00,
      "jurosTotal": 100.00
    }
  ],
  "dataPrimeiraParcela": "2025-01-03",
  "totalInteresses": 3,
  "disponivel": true
}
```

---

#### Buscar por Faixa de Valor

**GET** `/api/devedor/buscar-propostas/faixa/{faixa}`

Faixas disponíveis: `ate-1000`, `1000-5000`, `5000-10000`, `acima-10000`

**Request:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/buscar-propostas/faixa/1000-5000
```

---

#### Propostas com Menor Taxa

**GET** `/api/devedor/buscar-propostas/menor-taxa?limite=10`

**Request:**
```bash
curl -X GET "http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/buscar-propostas/menor-taxa?limite=5"
```

---

#### Propostas Mais Recentes

**GET** `/api/devedor/buscar-propostas/recentes?limite=10`

**Request:**
```bash
curl -X GET "http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/buscar-propostas/recentes?limite=10"
```

---

### 12. UC-D04: Selecionar Proposta

**POST** `/api/devedor/selecionar-proposta`

Demonstra interesse em uma proposta específica.

**Request:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/selecionar-proposta \
  -H "Content-Type: application/json" \
  -d '{
    "propostaId": 1,
    "devedorId": 1
  }'
```

**Response 201:**
```json
{
  "mensagem": "Interesse registrado com sucesso",
  "interesse": {
    "id": 1,
    "proposta": {
      "id": 1,
      "idPublico": "PROP-ABC123"
    },
    "devedor": {
      "id": 1,
      "nome": "Maria Santos"
    },
    "dataInteresse": "2024-12-03T15:00:00",
    "status": "PENDENTE",
    "confirmacaoCredor": false,
    "confirmacaoDevedor": false
  },
  "proximoPasso": "Aguarde a aprovação do credor"
}
```

**Response 400 (Dados incompletos):**
```json
{
  "erro": "Complete seus dados cadastrais (endereço, cidade, estado, CEP) antes de selecionar uma proposta"
}
```

**Response 409 (Interesse duplicado):**
```json
{
  "erro": "Você já demonstrou interesse nesta proposta"
}
```

---

#### Listar Meus Interesses

**GET** `/api/devedor/selecionar-proposta/meus/{devedorId}`

**Request:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/selecionar-proposta/meus/1
```

**Response 200:**
```json
[
  {
    "id": 1,
    "proposta": {
      "idPublico": "PROP-ABC123",
      "valorDisponivel": 5000.00,
      "nomeCredor": "João Silva"
    },
    "status": "PENDENTE",
    "dataInteresse": "2024-12-03T15:00:00"
  },
  {
    "id": 2,
    "proposta": {
      "idPublico": "PROP-XYZ789",
      "valorDisponivel": 3000.00,
      "nomeCredor": "Pedro Oliveira"
    },
    "status": "APROVADO",
    "dataInteresse": "2024-12-02T10:30:00"
  }
]
```

---

#### Listar Interesses Pendentes

**GET** `/api/devedor/selecionar-proposta/meus/{devedorId}/pendentes`

**Request:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/selecionar-proposta/meus/1/pendentes
```

---

#### Listar Interesses Aprovados

**GET** `/api/devedor/selecionar-proposta/meus/{devedorId}/aprovados`

**Request:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/selecionar-proposta/meus/1/aprovados
```

---

#### Cancelar Interesse

**DELETE** `/api/devedor/selecionar-proposta/{interesseId}`

**Request:**
```bash
curl -X DELETE http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/selecionar-proposta/1
```

**Response 200:**
```json
{
  "mensagem": "Interesse cancelado com sucesso"
}
```

**Response 400 (Não pode cancelar):**
```json
{
  "erro": "Só é possível cancelar interesses pendentes"
}
```

---

#### Detalhes do Interesse

**GET** `/api/devedor/selecionar-proposta/interesse/{interesseId}`

**Request:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/selecionar-proposta/interesse/1
```

**Response 200:**
```json
{
  "interesse": {
    "id": 1,
    "status": "APROVADO",
    "dataInteresse": "2024-12-03T15:00:00"
  },
  "proposta": {
    "idPublico": "PROP-ABC123",
    "valorDisponivel": 5000.00
  },
  "credor": "João Silva",
  "podeConfirmar": true,
  "podeCancelar": false
}
```

---

### 13. UC-D05: Pedir Empréstimo

**POST** `/api/devedor/pedir-emprestimo/{interesseId}/confirmar`

Confirma o pedido de empréstimo pelo devedor.

**Request:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/pedir-emprestimo/1/confirmar \
  -H "Content-Type: application/json" \
  -d '{
    "numeroParcelas": 12
  }'
```

**Response 200 (Aguardando credor):**
```json
{
  "mensagem": "Confirmação registrada. Aguardando confirmação do credor.",
  "interesse": {
    "id": 1,
    "status": "APROVADO",
    "confirmacaoCredor": false,
    "confirmacaoDevedor": true,
    "dataConfirmacaoDevedor": "2024-12-03T15:30:00"
  },
  "emprestimo": null
}
```

**Response 200 (Empréstimo criado):**
```json
{
  "mensagem": "Empréstimo criado com sucesso! O valor será disponibilizado em breve.",
  "interesse": {
    "id": 1,
    "status": "APROVADO",
    "confirmacaoCredor": true,
    "confirmacaoDevedor": true
  },
  "emprestimo": {
    "id": 1,
    "valorPrincipal": 5000.00,
    "jurosAplicados": 150.00,
    "valorTotal": 5150.00,
    "numeroParcelas": 12,
    "parcelasPagas": 0,
    "dataInicio": "2024-12-03",
    "dataVencimento": "2025-12-03",
    "status": "EM_ANDAMENTO",
    "parcelas": [
      {
        "numeroParcela": 1,
        "valor": 429.17,
        "dataVencimento": "2025-01-03",
        "paga": false,
        "atrasada": false
      }
    ]
  }
}
```

---

#### Simular Parcelas

**POST** `/api/devedor/pedir-emprestimo/{interesseId}/simular`

**Request:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/pedir-emprestimo/1/simular \
  -H "Content-Type: application/json" \
  -d '{
    "numeroParcelas": 12
  }'
```

**Response 200:**
```json
{
  "valorPrincipal": 5000.00,
  "juros": 150.00,
  "valorTotal": 5150.00,
  "numeroParcelas": 12,
  "valorParcela": 429.17,
  "taxaJuros": 2.5,
  "primeiraParcela": "2025-01-03",
  "ultimaParcela": "2025-12-03"
}
```

---

#### Listar Pedidos Pendentes

**GET** `/api/devedor/pedir-emprestimo/pendentes/{devedorId}`

**Request:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/pedir-emprestimo/pendentes/1
```

---

#### Listar Meus Empréstimos

**GET** `/api/devedor/pedir-emprestimo/meus/{devedorId}`

**Request:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/pedir-emprestimo/meus/1
```

**Response 200:**
```json
[
  {
    "id": 1,
    "credor": {
      "nome": "João Silva"
    },
    "valorPrincipal": 5000.00,
    "valorTotal": 5150.00,
    "numeroParcelas": 12,
    "parcelasPagas": 2,
    "status": "EM_ANDAMENTO",
    "dataInicio": "2024-12-03"
  }
]
```

---

#### Detalhes do Empréstimo

**GET** `/api/devedor/pedir-emprestimo/emprestimo/{emprestimoId}`

**Request:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/pedir-emprestimo/emprestimo/1
```

**Response 200:**
```json
{
  "emprestimo": {
    "id": 1,
    "valorPrincipal": 5000.00,
    "valorTotal": 5150.00,
    "numeroParcelas": 12,
    "parcelasPagas": 2
  },
  "parcelasRestantes": 10,
  "valorRestante": 4291.70,
  "percentualPago": 16.67
}
```

---

### 14. UC-D06: Aceitar Termos

**POST** `/api/devedor/aceitar-termos/{devedorId}`

Registra a aceitação dos termos de uso.

**Request:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/aceitar-termos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "aceitouTermos": true,
    "versaoTermos": "1.0",
    "ip": "192.168.1.1"
  }'
```

**Response 200:**
```json
{
  "mensagem": "Termos aceitos com sucesso",
  "dataAceitacao": "2024-12-03T15:30:00",
  "devedorId": 1,
  "devedorNome": "Maria Santos",
  "versaoTermos": "1.0",
  "proximoPasso": "Você já pode demonstrar interesse em propostas de empréstimo"
}
```

---

#### Obter Termos Atuais

**GET** `/api/devedor/aceitar-termos/termos-atuais`

**Request:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/aceitar-termos/termos-atuais
```

**Response 200:**
```json
{
  "versao": "1.0",
  "dataPublicacao": "2024-01-01",
  "titulo": "Termos de Uso - Plataforma de Empréstimos Agilit",
  "conteudo": "TERMOS DE USO...",
  "obrigatorio": true
}
```

---

#### Verificar Status de Aceitação

**GET** `/api/devedor/aceitar-termos/{devedorId}/status`

**Request:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/aceitar-termos/1/status
```

**Response 200:**
```json
{
  "devedorId": 1,
  "aceitouTermos": true,
  "versaoAceita": "1.0",
  "dataAceitacao": "2024-12-03T15:30:00",
  "precisaAceitar": false
}
```

---

## 🔄 FLUXOS COMPLETOS

### 15. Fluxo Completo do Credor

```bash
# 1. Criar Conta
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/criar-conta \
  -H "Content-Type: application/json" \
  -d '{"nome":"João Silva","cpf":"12345678900","email":"joao@email.com","senhaHash":"senha123","saldoDisponivel":10000.00}'

# 2. Fazer Login
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/login \
  -H "Content-Type: application/json" \
  -d '{"email":"joao@email.com","senha":"senha123"}'

# 3. Criar Oferta
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/criar-oferta \
  -H "Content-Type: application/json" \
  -d '{"credor":{"id":1},"valorDisponivel":5000.00,"parcelasMinimas":6,"parcelasMaximas":24,"taxaJuros":2.5,"diasAtePrimeiraCobranca":30}'

# 4. Gerar Proposta
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/gerar-proposta \
  -H "Content-Type: application/json" \
  -d '{"ofertaId":1}'

# 5. Aguardar interesse de devedor (sistema notifica automaticamente)

# 6. Aprovar interesse
curl -X PUT http://localhost:8080/Sistemas_Distriubidos_Agilit/api/interesse/1/aprovar

# 7. Confirmar empréstimo
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/registrar-emprestimo/1/confirmar \
  -H "Content-Type: application/json" \
  -d '{"numeroParcelas":12}'

# 8. Empréstimo criado automaticamente após confirmação bilateral ✅
```

---

### 16. Fluxo Completo do Devedor

```bash
# 1. Criar Conta
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/criar-conta \
  -H "Content-Type: application/json" \
  -d '{"nome":"Maria Santos","cpf":"98765432100","email":"maria@email.com","senhaHash":"senha123","endereco":"Rua das Flores, 123","cidade":"São Paulo","estado":"SP","cep":"01234-567"}'

# 2. Fazer Login
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/login \
  -H "Content-Type: application/json" \
  -d '{"email":"maria@email.com","senha":"senha123"}'

# 3. Aceitar Termos
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/aceitar-termos/1 \
  -H "Content-Type: application/json" \
  -d '{"aceitouTermos":true,"versaoTermos":"1.0"}'

# 4. Buscar Propostas
curl -X GET "http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/buscar-propostas?valorMin=1000&valorMax=5000"

# 5. Ver Detalhes e Simular
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/buscar-propostas/PROP-ABC123/detalhes
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/pedir-emprestimo/1/simular \
  -H "Content-Type: application/json" \
  -d '{"numeroParcelas":12}'

# 6. Selecionar Proposta
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/selecionar-proposta \
  -H "Content-Type: application/json" \
  -d '{"propostaId":1,"devedorId":1}'

# 7. Aguardar aprovação do credor (sistema notifica automaticamente)

# 8. Confirmar Pedido
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/pedir-emprestimo/1/confirmar \
  -H "Content-Type: application/json" \
  -d '{"numeroParcelas":12}'

# 9. Empréstimo criado automaticamente após confirmação bilateral ✅
```

---

### 17. Fluxo de Criação de Empréstimo

**Confirmação Bilateral Completa:**

```bash
# Passo 1: Devedor demonstra interesse
POST /api/devedor/selecionar-proposta
{"propostaId": 1, "devedorId": 1}

# Passo 2: Credor aprova interesse
PUT /api/interesse/1/aprovar

# Passo 3: Credor confirma com número de parcelas
POST /api/credor/registrar-emprestimo/1/confirmar
{"numeroParcelas": 12}

# Passo 4: Devedor confirma com número de parcelas
POST /api/devedor/pedir-emprestimo/1/confirmar
{"numeroParcelas": 12}

# Resultado: Empréstimo criado automaticamente! ✅
# - Saldo do credor é debitado
# - Parcelas são geradas automaticamente
# - Status da proposta muda para "ACEITA"
# - Ambos recebem notificações
```

---

## 📝 NOTAS IMPORTANTES

### Arquitetura Baseada em Casos de Uso

- **Um controller = Um caso de uso específico**
- Mapeamento direto com requisitos de negócio
- Código mais organizado e manutenível
- Facilita testes e documentação

### Endpoints Legados

Os endpoints antigos (CRUD genérico) foram mantidos para compatibilidade:
- `/api/credor` (CRUD)
- `/api/devedor` (CRUD)
- `/api/oferta`
- `/api/proposta`
- `/api/interesse`

**Recomendação:** Use os novos endpoints baseados em casos de uso.

### Confirmação Bilateral

O sistema requer confirmação de **ambas as partes** antes de criar o empréstimo:
1. Devedor demonstra interesse
2. Credor aprova interesse
3. Credor confirma (com número de parcelas)
4. Devedor confirma (com número de parcelas)
5. Sistema cria empréstimo automaticamente

### Notificações Automáticas

O sistema envia notificações automaticamente:
- ✉️ Novo interesse em proposta
- ✉️ Interesse aprovado/rejeitado
- ✉️ Confirmação pendente
- ✉️ Empréstimo criado

### Validações Importantes

- **Email e CPF**: Devem ser únicos
- **Senhas**: Hasheadas automaticamente
- **Dados do Devedor**: Endereço completo obrigatório para demonstrar interesse
- **Saldo do Credor**: Verificado ao criar oferta e confirmar empréstimo
- **Parcelas**: Devem estar dentro do range da proposta

### Headers Obrigatórios

```
Content-Type: application/json
Accept: application/json
```

### Códigos de Status HTTP

- **200 OK**: Sucesso
- **201 Created**: Recurso criado
- **204 No Content**: Sucesso sem conteúdo
- **400 Bad Request**: Dados inválidos
- **401 Unauthorized**: Não autenticado
- **404 Not Found**: Não encontrado
- **409 Conflict**: Conflito (ex: email já existe)
- **500 Internal Server Error**: Erro no servidor

---

## 📚 Documentação Adicional

- **API Completa**: `API_DOCUMENTATION_COMPLETE_V2.md`
- **Proposta de Estrutura**: `PROPOSTA_ESTRUTURA_CASOS_DE_USO.md`
- **Resumo de Controllers**: `RESUMO_CONTROLLERS_CASOS_DE_USO.md`
- **Próximos Passos**: `TODO_PROXIMOS_PASSOS.md`

---

**Versão:** 2.0  
**Data de Atualização:** 03/12/2024  
**Autor:** Sistema Agilit - Equipe de Desenvolvimento
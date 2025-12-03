# 📚 DOCUMENTAÇÃO COMPLETA DA API - Sistema de Empréstimos Agilit v2.0

> **Versão 2.0** - Atualizado com Controllers Baseados em Casos de Uso
> 
> Esta documentação reflete a nova arquitetura onde cada controller representa um caso de uso específico.

## 🌐 Base URL

```
http://localhost:8080/Sistemas_Distriubidos_Agilit/api
```

---

## 📋 ÍNDICE DE ROTAS

### 🔐 Autenticação
- [POST /api/auth/login](#post-apiauthlogin) - Login unificado (Credor ou Devedor)
- [GET /api/auth/verificar-email](#get-apiauthverificar-email) - Verificar disponibilidade de email
- [GET /api/auth/verificar-cpf](#get-apiauthverificar-cpf) - Verificar disponibilidade de CPF

### 👤 Credor - Casos de Uso

#### UC-C01: Criar Conta
- [POST /api/credor/criar-conta](#post-apicredorcriar-conta) - Criar nova conta de credor

#### UC-C02: Fazer Login
- [POST /api/credor/login](#post-apicredorlogin) - Autenticar credor

#### UC-C03: Criar Oferta de Empréstimo
- [POST /api/credor/criar-oferta](#post-apicredorcriar-oferta) - Criar oferta privada
- [GET /api/credor/criar-oferta/minhas/{credorId}](#get-apicredorcriar-ofertaminhascredorid) - Listar minhas ofertas
- [GET /api/credor/criar-oferta/minhas/{credorId}/ativas](#get-apicredorcriar-ofertaminhascredoridativas) - Listar ofertas ativas

#### UC-C04: Gerar Proposta de Empréstimo
- [POST /api/credor/gerar-proposta](#post-apicredorgerar-proposta) - Transformar oferta em proposta pública
- [GET /api/credor/gerar-proposta/minhas/{credorId}](#get-apicredorgerar-propostaminhascredorid) - Listar minhas propostas
- [GET /api/credor/gerar-proposta/minhas/{credorId}/ativas](#get-apicredorgerar-propostaminhascredoridativas) - Listar propostas ativas
- [PUT /api/credor/gerar-proposta/{propostaId}/cancelar](#put-apicredorgerar-propostapropostaidcancelar) - Cancelar proposta

#### UC-C05: Registrar Empréstimo
- [POST /api/credor/registrar-emprestimo/{interesseId}/confirmar](#post-apicredorregistrar-emprestimointeresseidconfirmar) - Confirmar empréstimo
- [GET /api/credor/registrar-emprestimo/pendentes/{credorId}](#get-apicredorregistrar-emprestimopendentescredorid) - Listar pendentes
- [GET /api/credor/registrar-emprestimo/meus/{credorId}](#get-apicredorregistrar-emprestimoMeuscredorid) - Listar meus empréstimos

#### Credor - Endpoints Legados (Mantidos para compatibilidade)
- [POST /api/credor](#post-apicredor) - Criar credor (CRUD genérico)
- [GET /api/credor](#get-apicredor) - Listar todos credores
- [GET /api/credor/{id}](#get-apicredorid) - Buscar credor por ID
- [PUT /api/credor/{id}](#put-apicredorid) - Atualizar credor
- [DELETE /api/credor/{id}](#delete-apicredorid) - Deletar credor
- [PUT /api/credor/saldo/{id}](#put-apicredorsaldoid) - Registrar saldo

### 👥 Devedor - Casos de Uso

#### UC-D01: Criar Conta
- [POST /api/devedor/criar-conta](#post-apidevedorcriar-conta) - Criar nova conta de devedor
- [PUT /api/devedor/criar-conta/{devedorId}/completar-dados](#put-apidevedorcriar-contadevedoridcompletar-dados) - Completar dados cadastrais

#### UC-D02: Fazer Login
- [POST /api/devedor/login](#post-apidevedorlogin) - Autenticar devedor

#### UC-D03: Buscar Propostas de Empréstimo
- [GET /api/devedor/buscar-propostas](#get-apidevedorbuscar-propostas) - Listar propostas com filtros
- [GET /api/devedor/buscar-propostas/{idPublico}](#get-apidevedorbuscar-propostasidpublico) - Buscar proposta específica
- [GET /api/devedor/buscar-propostas/{idPublico}/detalhes](#get-apidevedorbuscar-propostasidpublicodetalhes) - Detalhes com simulações
- [GET /api/devedor/buscar-propostas/faixa/{faixa}](#get-apidevedorbuscar-propostasfaixafaixa) - Buscar por faixa de valor
- [GET /api/devedor/buscar-propostas/menor-taxa](#get-apidevedorbuscar-propostasmenor-taxa) - Propostas com menor taxa
- [GET /api/devedor/buscar-propostas/recentes](#get-apidevedorbuscar-propostasrecentes) - Propostas mais recentes

#### UC-D04: Selecionar uma Proposta
- [POST /api/devedor/selecionar-proposta](#post-apidevedorselecionar-proposta) - Demonstrar interesse
- [GET /api/devedor/selecionar-proposta/meus/{devedorId}](#get-apidevedorselecionar-propostameusdevedorid) - Listar meus interesses
- [GET /api/devedor/selecionar-proposta/meus/{devedorId}/pendentes](#get-apidevedorselecionar-propostameusdevedoridpendentes) - Interesses pendentes
- [GET /api/devedor/selecionar-proposta/meus/{devedorId}/aprovados](#get-apidevedorselecionar-propostameusdevedoridaprovados) - Interesses aprovados
- [DELETE /api/devedor/selecionar-proposta/{interesseId}](#delete-apidevedorselecionar-propostainteresseid) - Cancelar interesse
- [GET /api/devedor/selecionar-proposta/interesse/{interesseId}](#get-apidevedorselecionar-propostainteresseinteresseid) - Detalhes do interesse

#### UC-D05: Pedir Empréstimo
- [POST /api/devedor/pedir-emprestimo/{interesseId}/confirmar](#post-apidevedorpedir-emprestimointeresseidconfirmar) - Confirmar pedido
- [GET /api/devedor/pedir-emprestimo/pendentes/{devedorId}](#get-apidevedorpedir-emprestimopendentesdevedorid) - Listar pendentes
- [GET /api/devedor/pedir-emprestimo/meus/{devedorId}](#get-apidevedorpedir-emprestimomeusdevedorid) - Listar meus empréstimos
- [GET /api/devedor/pedir-emprestimo/emprestimo/{emprestimoId}](#get-apidevedorpedir-emprestimoemprestimoemprestimoid) - Detalhes do empréstimo
- [POST /api/devedor/pedir-emprestimo/{interesseId}/simular](#post-apidevedorpedir-emprestimointeresseidsimular) - Simular parcelas

#### UC-D06: Aceitar Termos
- [POST /api/devedor/aceitar-termos/{devedorId}](#post-apidevedoraaceitar-termosdevedorid) - Aceitar termos de uso
- [GET /api/devedor/aceitar-termos/termos-atuais](#get-apidevedoraaceitar-termostermos-atuais) - Obter termos atuais
- [GET /api/devedor/aceitar-termos/{devedorId}/status](#get-apidevedoraaceitar-termosdevedoridstatus) - Verificar aceitação
- [GET /api/devedor/aceitar-termos/{devedorId}/historico](#get-apidevedoraaceitar-termosdevedoridhistorico) - Histórico de aceitação

#### Devedor - Endpoints Legados (Mantidos para compatibilidade)
- [POST /api/devedor](#post-apidevedor) - Criar devedor (CRUD genérico)
- [GET /api/devedor](#get-apidevedor) - Listar todos devedores
- [GET /api/devedor/{id}](#get-apidevedorid) - Buscar devedor por ID

### 💰 Oferta de Empréstimo (Endpoints Legados)
- [POST /api/oferta](#post-apioferta) - Criar oferta
- [GET /api/oferta/credor/{credorId}](#get-apiofertacredorcredorid) - Listar ofertas do credor
- [GET /api/oferta/credor/{credorId}/ativas](#get-apiofertacredorcredoridativas) - Listar ofertas ativas
- [GET /api/oferta/{id}](#get-apiofertaid) - Buscar oferta por ID
- [DELETE /api/oferta/{id}](#delete-apiofertaid) - Deletar oferta
- [POST /api/oferta/{id}/criar-proposta](#post-apiofertaidcriar-proposta) - Criar proposta
- [GET /api/oferta/{id}/opcoes-parcelas](#get-apiofertaidopcoes-parcelas) - Calcular opções

### 📢 Proposta de Empréstimo (Endpoints Legados)
- [GET /api/proposta/publicas](#get-apipropostapublicas) - Listar propostas públicas
- [GET /api/proposta/credor/{credorId}](#get-apipropostacredorcredorid) - Propostas do credor
- [GET /api/proposta/publico/{idPublico}](#get-apipropostapublicoidpublico) - Buscar por ID público
- [GET /api/proposta/{id}](#get-apipropostaid) - Buscar por ID
- [PUT /api/proposta/{id}/cancelar](#put-apipropostaidcancelar) - Cancelar proposta
- [GET /api/proposta/{id}/detalhes](#get-apipropostaiddetalhes) - Detalhes completos
- [GET /api/proposta/status/{status}](#get-apipropostastatusstatus) - Filtrar por status
- [GET /api/proposta/buscar](#get-apipropostabuscar) - Buscar por valor
- [GET /api/proposta/{id}/estatisticas](#get-apipropostaid estatisticas) - Estatísticas

### ❤️ Interesse em Proposta (Endpoints Legados)
- [POST /api/interesse](#post-apiinteresse) - Demonstrar interesse
- [GET /api/interesse/proposta/{propostaId}](#get-apiinteressepropostapropostaid) - Listar interessados
- [GET /api/interesse/devedor/{devedorId}](#get-apiinteressedevedordevedorid) - Meus interesses
- [PUT /api/interesse/{id}/aprovar](#put-apiinteresseidaprovar) - Credor aprova
- [PUT /api/interesse/{id}/rejeitar](#put-apiinteresseidrejeitar) - Credor rejeita
- [POST /api/interesse/{id}/confirmar-credor](#post-apiinteresseidconfirmar-credor) - Credor confirma
- [POST /api/interesse/{id}/confirmar-devedor](#post-apiinteresseidconfirmar-devedor) - Devedor confirma
- [DELETE /api/interesse/{id}](#delete-apiinteresseid) - Cancelar interesse

### 💳 Empréstimo
- [GET /api/emprestimo](#get-apiemprestimo) - Listar todos
- [GET /api/emprestimo/{id}](#get-apiemprestimoid) - Buscar por ID
- [POST /api/emprestimo](#post-apiemprestimo) - Criar empréstimo
- [PUT /api/emprestimo/{id}](#put-apiemprestimoid) - Atualizar empréstimo
- [DELETE /api/emprestimo/{id}](#delete-apiemprestimoid) - Deletar empréstimo

### 📅 Parcela
- [GET /api/parcela/emprestimo/{emprestimoId}](#get-apiparcelaemprestimoemprestimoid) - Listar parcelas
- [GET /api/parcela/{id}](#get-apiparcelaid) - Buscar parcela
- [PUT /api/parcela/{id}/pagar](#put-apiparcelaidpagar) - Marcar como paga
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
- [GET /api/notificacao/{id}](#get-apinotificacaoid) - Buscar por ID
- [PUT /api/notificacao/{id}/marcar-lida](#put-apinotificacaoidmarcar-lida) - Marcar como lida
- [PUT /api/notificacao/{tipoDestinatario}/{destinatarioId}/marcar-todas-lidas](#put-apinotificacaotipodestinatariodestinatarioidmarcar-todas-lidas) - Marcar todas
- [DELETE /api/notificacao/{id}](#delete-apinotificacaoid) - Deletar notificação
- [DELETE /api/notificacao/{tipoDestinatario}/{destinatarioId}/lidas](#delete-apinotificacaotipodestinatariodestinatarioidlidas) - Deletar lidas
- [GET /api/notificacao/{tipoDestinatario}/{destinatarioId}/tipo/{tipo}](#get-apinotificacaotipodestinatariodestinatarioidtipotipo) - Filtrar por tipo
- [GET /api/notificacao/{tipoDestinatario}/{destinatarioId}/recentes](#get-apinotificacaotipodestinatariodestinatarioidrecentes) - Recentes

---

## 🆕 NOVOS ENDPOINTS - CASOS DE USO

### 🔐 AUTENTICAÇÃO

#### POST /api/auth/login
**Descrição:** Login unificado para Credor ou Devedor

**Request Body:**
```json
{
  "email": "usuario@email.com",
  "senha": "senha123",
  "tipo": "CREDOR"  // ou "DEVEDOR"
}
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

---

### 👤 CREDOR - CASOS DE USO

#### POST /api/credor/criar-conta
**UC-C01: Criar Conta de Credor**

**Request Body:**
```json
{
  "nome": "João Silva",
  "cpf": "12345678900",
  "email": "joao@email.com",
  "senhaHash": "senha123",
  "telefone": "(11) 98765-4321",
  "saldoDisponivel": 10000.00
}
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

**Validações:**
- Nome, email, senha e CPF são obrigatórios
- Email deve ser único
- CPF deve ser único
- Senha é automaticamente hasheada
- Saldo inicial padrão: R$ 0,00

---

#### POST /api/credor/login
**UC-C02: Fazer Login (Credor)**

**Request Body:**
```json
{
  "email": "joao@email.com",
  "senha": "senha123"
}
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

#### POST /api/credor/criar-oferta
**UC-C03: Criar Oferta de Empréstimo**

**Request Body:**
```json
{
  "credor": { "id": 1 },
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 6,
  "parcelasMaximas": 24,
  "taxaJuros": 2.5,
  "diasAtePrimeiraCobranca": 30
}
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

**Validações:**
- Credor deve existir
- Valor disponível > 0
- Parcelas mínimas >= 1
- Parcelas máximas >= parcelas mínimas
- Taxa de juros >= 0
- Credor deve ter saldo suficiente

---

#### GET /api/credor/criar-oferta/minhas/{credorId}
**Listar Minhas Ofertas**

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
  }
]
```

---

#### POST /api/credor/gerar-proposta
**UC-C04: Gerar Proposta de Empréstimo**

**Request Body:**
```json
{
  "ofertaId": 1,
  "taxaJuros": 2.5  // Opcional: ajustar juros
}
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

**Regras:**
- Oferta deve existir e estar ativa
- Gera ID público único (formato: PROP-XXXXXX)
- Permite ajustar taxa de juros ao gerar proposta
- Proposta herda dados da oferta

---

#### POST /api/credor/registrar-emprestimo/{interesseId}/confirmar
**UC-C05: Registrar Empréstimo (Confirmar pelo Credor)**

**Request Body:**
```json
{
  "numeroParcelas": 12
}
```

**Response 200 (Aguardando devedor):**
```json
{
  "mensagem": "Confirmação registrada. Aguardando confirmação do devedor.",
  "interesse": {
    "id": 1,
    "status": "APROVADO",
    "confirmacaoCredor": true,
    "confirmacaoDevedor": false
  },
  "emprestimo": null
}
```

**Response 200 (Empréstimo criado):**
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
    "status": "EM_ANDAMENTO"
  }
}
```

---

### 👥 DEVEDOR - CASOS DE USO

#### POST /api/devedor/criar-conta
**UC-D01: Criar Conta de Devedor**

**Request Body:**
```json
{
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "email": "maria@email.com",
  "senhaHash": "senha123",
  "telefone": "(11) 91234-5678",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567"
}
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

#### PUT /api/devedor/criar-conta/{devedorId}/completar-dados
**Completar Dados Cadastrais**

**Request Body:**
```json
{
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567",
  "telefone": "(11) 91234-5678"
}
```

---

#### POST /api/devedor/login
**UC-D02: Fazer Login (Devedor)**

**Request Body:**
```json
{
  "email": "maria@email.com",
  "senha": "senha123"
}
```

**Response 200:**
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

**Se dados incompletos:**
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

#### GET /api/devedor/buscar-propostas
**UC-D03: Buscar Propostas de Empréstimo**

**Query Parameters:**
- `valorMin` (opcional): Valor mínimo
- `valorMax` (opcional): Valor máximo
- `parcelasMin` (opcional): Parcelas mínimas
- `parcelasMax` (opcional): Parcelas máximas
- `taxaJurosMax` (opcional): Taxa de juros máxima

**Exemplo:**
```
GET /api/devedor/buscar-propostas?valorMin=1000&valorMax=5000&taxaJurosMax=3.0
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
  }
]
```

---

#### GET /api/devedor/buscar-propostas/{idPublico}/detalhes
**Detalhes Completos com Simulações**

**Response 200:**
```json
{
  "proposta": {
    "id": 1,
    "idPublico": "PROP-ABC123",
    "valorDisponivel": 5000.00,
    "taxaJuros": 2.5
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
    }
  ],
  "dataPrimeiraParcela": "2025-01-03",
  "totalInteresses": 3,
  "disponivel": true
}
```

---

#### GET /api/devedor/buscar-propostas/faixa/{faixa}
**Buscar por Faixa de Valor**

**Faixas disponíveis:**
- `ate-1000`: até R$ 1.000
- `1000-5000`: R$ 1.000 a R$ 5.000
- `5000-10000`: R$ 5.000 a R$ 10.000
- `acima-10000`: acima de R$ 10.000

**Exemplo:**
```
GET /api/devedor/buscar-propostas/faixa/1000-5000
```

---

#### GET /api/devedor/buscar-propostas/menor-taxa
**Propostas com Menor Taxa de Juros**

**Query Parameters:**
- `limite` (opcional, padrão: 10): Número máximo de resultados

---

#### POST /api/devedor/selecionar-proposta
**UC-D04: Selecionar uma Proposta (Demonstrar Interesse)**

**Request Body:**
```json
{
  "propostaId": 1,
  "devedorId": 1
}
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

**Validações:**
- Proposta deve estar ativa
- Devedor deve ter dados completos (endereço, cidade, estado, CEP)
- Devedor não pode ter interesse duplicado na mesma proposta
- Notifica o credor automaticamente

---

#### GET /api/devedor/selecionar-proposta/meus/{devedorId}
**Listar Meus Interesses**

**Response 200:**
```json
[
  {
    "id": 1,
    "proposta": {
      "idPublico": "PROP-ABC123",
      "valorDisponivel": 5000.00
    },
    "status": "PENDENTE",
    "dataInteresse": "2024-12-03T15:00:00"
  }
]
```

---

#### POST /api/devedor/pedir-emprestimo/{interesseId}/confirmar
**UC-D05: Pedir Empréstimo (Confirmar pelo Devedor)**

**Request Body:**
```json
{
  "numeroParcelas": 12
}
```

**Response 200 (Aguardando credor):**
```json
{
  "mensagem": "Confirmação registrada. Aguardando confirmação do credor.",
  "interesse": {
    "id": 1,
    "status": "APROVADO",
    "confirmacaoCredor": false,
    "confirmacaoDevedor": true
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
        "paga": false
      }
    ]
  }
}
```

---

#### POST /api/devedor/pedir-emprestimo/{interesseId}/simular
**Simular Parcelas Antes de Confirmar**

**Request Body:**
```json
{
  "numeroParcelas": 12
}
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

#### POST /api/devedor/aceitar-termos/{devedorId}
**UC-D06: Aceitar Termos de Uso**

**Request Body:**
```json
{
  "aceitouTermos": true,
  "versaoTermos": "1.0",
  "ip": "192.168.1.1"
}
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

#### GET /api/devedor/aceitar-termos/termos-atuais
**Obter Termos de Uso Atuais**

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

## 🔄 FLUXO COMPLETO DO SISTEMA (Atualizado)

### Fluxo do Credor

```
1. Criar Conta
   POST /api/credor/criar-conta
   
2. Fazer Login
   POST /api/credor/login
   
3. Criar Oferta Privada
   POST /api/credor/criar-oferta
   
4. Gerar Proposta Pública
   POST /api/credor/gerar-proposta
   
5. Aguardar Interesse de Devedor
   (Sistema notifica automaticamente)
   
6. Aprovar Interesse
   PUT /api/interesse/{id}/aprovar
   
7. Confirmar Empréstimo
   POST /api/credor/registrar-emprestimo/{interesseId}/confirmar
   
8. Empréstimo Criado Automaticamente ✅
```

### Fluxo do Devedor

```
1. Criar Conta
   POST /api/devedor/criar-conta
   
2. Fazer Login
   POST /api/devedor/login
   
3. Aceitar Termos
   POST /api/devedor/aceitar-termos/{devedorId}
   
4. Completar Dados (se necessário)
   PUT /api/devedor/criar-conta/{devedorId}/completar-dados
   
5. Buscar Propostas
   GET /api/devedor/buscar-propostas
   
6. Ver Detalhes e Simular
   GET /api/devedor/buscar-propostas/{idPublico}/detalhes
   POST /api/devedor/pedir-emprestimo/{interesseId}/simular
   
7. Selecionar Proposta (Demonstrar Interesse)
   POST /api/devedor/selecionar-proposta
   
8. Aguardar Aprovação do Credor
   (Sistema notifica automaticamente)
   
9. Confirmar Pedido de Empréstimo
   POST /api/devedor/pedir-emprestimo/{interesseId}/confirmar
   
10. Empréstimo Criado Automaticamente ✅
```

---

## ⚠️ CÓDIGOS DE STATUS HTTP

- **200 OK**: Requisição bem-sucedida
- **201 Created**: Recurso criado com sucesso
- **204 No Content**: Operação bem-sucedida sem conteúdo de retorno
- **400 Bad Request**: Dados inválidos ou faltando
- **401 Unauthorized**: Não autenticado
- **404 Not Found**: Recurso não encontrado
- **409 Conflict**: Conflito (ex: email já cadastrado)
- **500 Internal Server Error**: Erro no servidor

---

## 📝 OBSERVAÇÕES IMPORTANTES

### Arquitetura Baseada em Casos de Uso

A nova estrutura organiza os controllers por casos de uso específicos:

- **Um controller = Um caso de uso**
- Mapeamento direto com requisitos de negócio
- Código mais organizado e manutenível
- Facilita testes e documentação

### Endpoints Legados

Os endpoints antigos (CRUD genérico) foram mantidos para compatibilidade, mas recomenda-se usar os novos endpoints baseados em casos de uso.

### Confirmação Bilateral

O sistema requer confirmação de ambas as partes (credor e devedor) antes de criar o empréstimo:

1. Devedor demonstra interesse
2. Credor aprova interesse
3. Credor confirma com número de parcelas
4. Devedor confirma com número de parcelas
5. Sistema cria empréstimo automaticamente

### Notificações Automáticas

O sistema envia notificações automaticamente em eventos importantes:
- Novo interesse em proposta
- Interesse aprovado/rejeitado
- Confirmação pendente
- Empréstimo criado

### Validações de Dados

- **Credor**: Email e CPF únicos, saldo suficiente para ofertas
- **Devedor**: Email e CPF únicos, dados completos para demonstrar interesse
- **Senhas**: Automaticamente hasheadas com PasswordUtil
- **Parcelas**: Devem estar dentro do range definido na proposta

---

## 🎯 EXEMPLOS PRÁTICOS COMPLETOS

### Exemplo 1: Credor Cria Oferta e Gera Proposta

```bash
# 1. Criar conta
POST /api/credor/criar-conta
{
  "nome": "João Silva",
  "cpf": "12345678900",
  "email": "joao@email.com",
  "senhaHash": "senha123",
  "saldoDisponivel": 10000.00
}

# 2. Login
POST /api/credor/login
{
  "email": "joao@email.com",
  "senha": "senha123"
}

# 3. Criar oferta
POST /api/credor/criar-oferta
{
  "credor": { "id": 1 },
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 6,
  "parcelasMaximas": 24,
  "taxaJuros": 2.5,
  "diasAtePrimeiraCobranca": 30
}

# 4. Gerar proposta pública
POST /api/credor/gerar-proposta
{
  "ofertaId": 1
}
```

### Exemplo 2: Devedor Busca e Seleciona Proposta

```bash
# 1. Criar conta
POST /api/devedor/criar-conta
{
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "email": "maria@email.com",
  "senhaHash": "senha123",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567"
}

# 2. Login
POST /api/devedor/login
{
  "email": "maria@email.com",
  "senha": "senha123"
}

# 3. Aceitar termos
POST /api/devedor/aceitar-termos/1
{
  "aceitouTermos": true,
  "versaoTermos": "1.0"
}

# 4. Buscar propostas
GET /api/devedor/buscar-propostas?valorMin=1000&valorMax=5000

# 5. Ver detalhes
GET /api/devedor/buscar-propostas/PROP-ABC123/detalhes

# 6. Simular parcelas
POST /api/devedor/pedir-emprestimo/1/simular
{
  "numeroParcelas": 12
}

# 7. Demonstrar interesse
POST /api/devedor/selecionar-proposta
{
  "propostaId": 1,
  "devedorId": 1
}
```

### Exemplo 3: Confirmação Bilateral e Criação de Empréstimo

```bash
# 1. Credor aprova interesse
PUT /api/interesse/1/aprovar

# 2. Credor confirma empréstimo
POST /api/credor/registrar-emprestimo/1/confirmar
{
  "numeroParcelas": 12
}

# 3. Devedor confirma empréstimo
POST /api/devedor/pedir-emprestimo/1/confirmar
{
  "numeroParcelas": 12
}

# 4. Empréstimo criado automaticamente! ✅
```

---

## 📚 Documentação Adicional

- **Proposta Completa**: `PROPOSTA_ESTRUTURA_CASOS_DE_USO.md`
- **Resumo de Controllers**: `RESUMO_CONTROLLERS_CASOS_DE_USO.md`
- **Próximos Passos**: `TODO_PROXIMOS_PASSOS.md`
- **Exemplos de Requisições**: `EXEMPLOS_REQUISICOES_COMPLETO.md`

---

**Versão:** 2.0  
**Data de Atualização:** 03/12/2024  
**Autor:** Sistema Agilit - Equipe de Desenvolvimento
# Checkpoint 5 — Bug Hunt PetFiap



## Identificação

**Grupo:** ___

| Integrante                 | RM     | Turma |
|----------------------------|--------|-------|
| Luiz Henrique Barbosa Dias | 562399 | 2CCPO |
| Gregory Debom Ferreira     | 562346 | 2CCPO |


| Campo |                     |
|---|---------------------|
| **Total de bugs corrigidos** | 12 / 12             |
| **Total de ajustes de Clean Code** | 6 / 6               |
| **Total de testes novos escritos** | 6 / 6               |
| **Suíte final (Run As → JUnit Test)** | 26 testes, 0 falhas |

---

## Parte 1 — Bugs encontrados

> Uma linha por bug, na ordem em que você os encontrou. Use a numeração dos seus
> commits (`fix: bug01 ...`). Preencha TODAS as colunas — metade da nota está aqui.

| # | Sintoma observado (o que fiz/vi) | Causa raiz (arquivo e linha aproximada) | Correção aplicada | Conceito da disciplina |
|---|---|---|---|---|
| bug01 |	O atendimento era criado pelo Builder com o nome do pet nulo |o parâmetro petNome não era atribuído ao atributo da classe, faltava o this. |Passei a atribuir this.petNome = petNome |Palavra-chave this e escopo de variáveis; padrão Builder|
| bug02 |Ao pedir o tipo TOSA, o sistema não criava uma Tosa de verdade. |AtendimentoFactory.criar, case "TOSA"  instanciava a classe errada. |Corrigi o case para new Tosa(...) | Padrão Factory e polimorfismo|
| bug03 |	Uma ConsultaVeterinaria nascia com dados incorretos. | Construtor de ConsultaVeterinaria inicialização errada dos atributos herdados.|Corrigi o construtor para repassar corretamente os dados ao super(...) |Herança e construtores com super |
| bug04 |	O agendamento duplicado (mesmo pet e mesmo horário) passava pela verificação de conflito |AgendaService.agendar  comparava data/hora (e nome) com ==, que compara referências e não o conteúdo. |Troquei == por .equals() |== vs .equals() |
| bug05 |	Buscar um id inexistente não lançava exceção: retornava null, e concluir/cancelar quebravam com NullPointerException.| AgendaService.buscarPorId o catch (Exception e) engolia a AtendimentoNaoEncontradoException e devolvia null.|Removi o try/catch, deixando o orElseThrow propagar a exceção |Exceções customizadas unchecked e propagação de exceções |
| bug06 | Os protocolos não eram sequenciais (repetiam o mesmo número) e getInstancia() não devolvia sempre o mesmo objeto.|GeradorProtocolo.getInstancia  criava um new GeradorProtocolo() sem guardar na variável estática instancia. | Passei a atribuir a nova instância à variável estática|Padrão Singleton e atributos static |
| bug07 |O Builder aceitava construir um atendimento sem nome do pet ou sem porte. |AtendimentoBuilder.construir não validava os campos obrigatórios. |Adicionei validação dos campos obrigatórios, lançando IllegalArgumentException |Padrão Builder, validação de entrada e exceções |
| bug08 |	O preço do Banho por porte não batia com a tabela do contrato. |Banho.calcularPreco valores incorretos por porte. |Corrigi para R$ 60 (PEQUENO), R$ 80 (MEDIO) e R$ 100 (GRANDE) |Regras de negócio no model e polimorfismo (método abstrato calcularPreco). |
| bug09 |	A duração da Tosa, usada como Atendimento, voltava 30 minutos em vez de 60. |Tosa.getDuracaoMinutos(String porte)  o parâmetro inútil criou uma sobrecarga em vez de sobrescrever o método da classe base. |Removi o parâmetro e adicionei @Override |Sobrescrita (override) vs sobrecarga (overload) e @Override |
| bug10 |	Era possível cancelar um atendimento já CONCLUIDO ou já CANCELADO. |Atendimento.cancelar mudava o status sem validar o status atual. |Adicionei a validação de status, lançando StatusInvalidoException como o concluir() já fazia |Encapsulamento das regras de negócio no model e exceções customizada|
| bug11 |O @Id da entidade não tinha estratégia de geração: salvar um atendimento novo (id nulo) falharia no banco. |@Id sem @GeneratedValue. |Configurei a estratégia de geração automática de chave primária (auto-incremento) |Mapeamento JPA/Spring Data |
| bug12 |	A API aceitava agendar um atendimento com data/hora no passado. |AgendaService.agendar não havia validação de data retroativa. |Adicionei a validação no início do método, lançando IllegalArgumentException antes de consultar o repository. |Regras de negócio na camada de serviço e exceções |

## Parte 2 — Ajustes de Clean Code

| # | Onde estava | Qual princípio/boas práticas era violado | O que eu mudei |
|---|---|---|---|
| clean01 |AtendimentoFactory.criar |	Nomes sem significado: parâmetros abreviados (de uma letra) não revelam a intenção. |Renomeei os parâmetros para nomes descritivos (protocolo, tipo, petNome, petPorte, tutorNome, dataHora). |
| clean02 |GeradorProtocolo (construtor) |Efeito colateral no construtor: um System.out.println misturava saída de console com a criação do objeto. |Removi o log do construtor, que passou a ter só a função de impedir o new externo do Singleton. O método continua chamado proximo(), porque o GeradorProtocoloTest entregue depende desse nome. |
| clean03 |AtendimentoController |Código morto: o método calcularDescontoFidelidade nunca era chamado (e ainda não seguia a regra descrita no comentário). |Removi o método não utilizado. |
| clean04 |AgendaService.agendar |Efeito colateral e mistura de responsabilidades: System.out.println com dados do tutor dentro da regra de agendamento. |Removi o print do console do método agendar. |
| clean05 |AtendimentoBuilder.construir |Duplicação de código (DRY): cinco if quase idênticos, e um comentário desatualizado dizendo que a validação era do controller. |Extraí os métodos validarCamposObrigatorios e exigirPreenchido e corrigi o comentário. |
| clean06 |AtendimentoController |Números mágicos: 201 e 409 espalhados pelo código, e comentário de exemplo desatualizado no agendar. |Criei as constantes STATUS_CRIADO e STATUS_CONFLITO e corrigi o comentário. |

## Parte 3 — Testes novos (regras que estavam sem cobertura)

> Uma linha por teste novo (`test: ...`). "Regra coberta" é o comportamento do
> contrato (seção 3 do enunciado) que o teste protege. Em "Resultado", diga se o
> teste ficou vermelho ao ser escrito (revelou bug — qual?) ou verde de cara
> (regra já estava correta).

| # | Teste escrito (classe.método) | Regra coberta | Resultado ao escrever (vermelho/verde) |
|---|---|---|---|
| teste01 |BanhoPrecoTest.deveCobrarPrecoDoBanhoConformeOPorte |Banho custa R$ 60 (PEQUENO), R$ 80 (MEDIO) e R$ 100 (GRANDE). |Verde: o bug08 já estava corrigido; o teste protege contra regressão. |
| teste02 |TosaDuracaoTest.deveDurar60MinutosQuandoUsadaComoAtendimento |	Tosa dura 60 minutos, inclusive quando usada pela referência da classe base. |Verde: o bug09 já estava corrigido; o teste protege contra regressão. |
| teste03 |ConsultaPrecoTest.deveCobrar150ReaisIndependenteDoPorte |Consulta tem preço fixo de R$ 150, qualquer que seja o porte. |Verde de cara: a regra já estava correta. |
| teste04 |AgendaServiceDataPassadoTest.deveRecusarAgendamentoComDataNoPassado |Agendar no passado lança IllegalArgumentException e o banco nem é consultado. |Verde: o bug12 já estava corrigido; o teste protege contra regressão. |
| teste05 |AgendaServiceCancelamentoTest.deveRecusarCancelamentoDeAtendimentoJaConcluido |Cancelar um atendimento CONCLUIDO é recusado com StatusInvalidoException e nada é salvo. |Verde: o bug10 já estava corrigido; o teste protege contra regressão. |
| teste06 |	AtendimentoFactoryTipoTest.deveRecusarTipoInexistenteComMensagemClara |Tipo inexistente (ex.: "VACINA") é recusado com mensagem clara. |Verde de cara: a regra já estava correta. |

---

## Parte 4 — Perguntas de reflexão

> Responda com suas palavras, 5 a 10 linhas cada, **usando o código real do
> projeto como exemplo**. Respostas genéricas de tutorial não pontuam.

### 1. A suíte como contrato (Aula 15)
O projeto chegou com 20 testes, 9 vermelhos. Descreva como você usou as
mensagens de falha (ex.: `expected: <Rex> but was: <null>`) para caçar os bugs.
O que a suíte de testes tem de melhor do que testar tudo na mão com curl?

R: Cada teste vermelho mostrava o que era esperado e o que veio, e isso apontava 
para o bug. Por exemplo: os protocolos deveriam ser 1, 2, 3, mas recomeçavam do 1 (bug06);
o horário ocupado não lançava HorarioOcupadoException (bug04); e o id inexistente devolvia
null em vez de lançar exceção (bug05). A suíte é melhor que o curl porque roda em segundos,
sem banco e sem a API no ar, testa também os erros e dá para rodar tudo de novo a cada correção 
para ver se nada quebrou.
---
### 2. Mock e injeção de dependência (Aulas 13 a 15)
No `AgendaServiceTest`, o `@Mock` cria um `AtendimentoRepository` falso e o
`@InjectMocks` o injeta no service. Explique a relação disso com o `@Autowired`
que o Spring faz em produção — quem "injeta" em cada mundo, e por que o teste
consegue rodar sem banco e sem subir o Spring?

R: Em produção, o Spring cria o repositório real (ligado ao Oracle) e entrega ao 
AgendaService pelo @Autowired. No teste, o Mockito faz esse papel: o @Mock cria um 
repositório falso e o @InjectMocks entrega ele ao service. Como o service só conhece 
o tipo AtendimentoRepository, ele não percebe a diferença, e por isso o teste roda sem 
banco e sem subir o Spring.
---
### 3. `==` vs `.equals()` (Aula 7)
Um dos bugs fazia o agendamento duplicado passar pela verificação de conflito.
Explique por que `==` entre Strings e `LocalDateTime` falhou aqui, por que ele
"funciona por sorte" com literais como `"Rex"`, e o que a sua correção mudou.

R: O bug04 usava == para comparar a data e o nome no AgendaService. O == só verifica 
se é o mesmo objeto na memória, não se o conteúdo é igual. A data de um novo agendamento 
é outro objeto, então o resultado era falso e o conflito passava. Com "Rex" escrito direto 
no código funciona por sorte, porque o Java reaproveita textos iguais. Troquei por .equals(), 
que compara o conteúdo.
---
### 4. Sobrescrita vs sobrecarga (Aula 7)
Um dos bugs compilava sem nenhum erro: um método parecia sobrescrever
`getDuracaoMinutos`, mas na verdade criava uma assinatura nova. Explique a
diferença entre override e overload nesse caso e por que a anotação `@Override`
teria impedido o bug.

R: Na Tosa, o método getDuracaoMinutos(String porte) tinha um parâmetro a mais que
o da classe Atendimento, então era sobrecarga (um método novo) e não sobrescrita.
Por isso a duração vinha 30 em vez de 60. Com @Override, o compilador daria erro na hora,
avisando que o método não sobrescreve nada. Corrigi removendo o parâmetro e colocando @Override.
---
### 5. Singleton manual vs bean do Spring (Aula 14)
O `GeradorProtocolo` é um Singleton escrito à mão e causou um dos bugs.
Explique o que ele garante, qual foi o bug, e por que o `AgendaService`
(`@Service`) não corre o mesmo risco no container do Spring.

R: O Singleton garante uma única instância do gerador, para a numeração
dos protocolos ser uma só. O bug06 criava um gerador novo a cada getInstancia()
sem guardar na variável estática, então os protocolos se repetiam. 
O AgendaService não tem esse risco porque o Spring já cria uma única instância de cada 
@Service e entrega a mesma onde ele é injetado, sem código manual.
---
### 6. Cobertura de testes: onde parar? (Aula 15)
Dos 6 testes novos que você escreveu, alguns ficaram vermelhos (revelaram
bugs) e outros verdes de cara (regras já corretas). Vale a pena manter os que
ficaram verdes? Em um projeto real com prazo, o que você priorizaria testar:
caminho feliz, caminhos de erro, ou 100% de cobertura? Justifique.


R: Os 6 testes novos ficaram verdes de cara, porque os bugs 08, 09, 10 e 12 já estavam corrigidos, 
e dois deles cobrem regras que já estavam certas. Vale manter os verdes, 
porque protegem contra regressão e custam pouco. Com prazo curto, eu priorizaria
as regras de preço e de status e os caminhos de erro (onde estavam os bugs) antes de 
buscar 100% de cobertura.

---


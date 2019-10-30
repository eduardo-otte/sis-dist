# Sistemas Distribuídos
Trabalhos da matéria de Sistemas Distribuídos

## Trabalhos
1. Seminários - 10/09/2019
2. Desenvolvimento de aplicação utilizando _sockets_ - 17/09/2019
3. Desenvolvimento de aplicação usando objetos remotos (Java RMI) - 22/10/2019
4. Desenvolvimento de _middleware_ para sistema heterogêneo - 12/11/2019

### Seminário
Apresentação dos conceitos básicos de _serverless_.

### Desenvolvimento de aplicação utilizando sockets
Implementação do algoritmo [_phase king_](https://www.cs.uic.edu/~ajayk/Chapter14.pdf) (slide 17). Conjunto de 5 processos diferentes (n = 5), com no máximo uma falha (f = 1), realizando uma votação para decidir um valor. Obrigatório implementar os princípios básicos do algoritmo (término, validação, integridade e acordo).

### Desenvolvimento de aplicação usando objetos remotos (Java RMI)
Implementação de um sistema de cadastro de empregos utilizando invocação de objetos remotos, através da _middleware_ **Java RMI**. O sistema possui um servidor, um cliente para companhias que ofertam vagas de emprego, e um cliente para profissionais que buscam um emprego.
- Servidor: provê os métodos de cadastro, consulta, atualização e notificação, tanto para currículos, quanto para vagas de emprego;
- Cliente de companhia: invoca os métodos do servidor referentes ao cadastro e atualização de vagas de emprego, e consulta e notificação de currículos. Provê um método de _callback_ para que o servidor possa lhe enviar notificações;
- Cliente de aplicante: invoca os métodos do servidor referentes ao cadastro e atualização de curríuclos, e consulta e notificação de vagas de emprego. Provê um método de _callback_ para que o servidor possa lhe enviar notificações.

### Desenvolvimento de _middleware_ para sistema heterogêneo
Implementação do mesmo sistema do trabalho anterior, mas com linguagens diferentes. O servidor é desenvolvido em **NodeJS**, e implementa uma **API Rest**. Os clientes são desenvolvidos em **Python**. Nesse projeto, o servidor precisa enviar notificações para os clientes. Logo, os clientes não expõem nenhum _endpoint_, enquanto o servidor expõe somente _endpoints_ referentes a consulta, cadastro e atualização de currículos e ofertas de vagas de emprego.

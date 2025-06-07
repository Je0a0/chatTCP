# Chat TCP com Interface Gráfica

Este é um sistema de chat em tempo real desenvolvido em Java, utilizando sockets TCP e interface gráfica Swing. O sistema permite comunicação entre múltiplos usuários através de um servidor central.

## Funcionalidades

- Interface gráfica para servidor e cliente
- Suporte a múltiplos usuários simultâneos
- Notificações de entrada e saída de usuários
- Mensagens em tempo real
- Conexão local e remota

## Requisitos

- Java 8 ou superior
- Sistema operacional com suporte a Java

## Como Executar

### Servidor

1. Compile o servidor:
```bash
javac ChatServerGUI.java
```

2. Execute o servidor:
```bash
java ChatServerGUI
```

O servidor iniciará na porta 4444 por padrão.

### Cliente

1. Compile o cliente:
```bash
javac ChatClientGUI.java
```

2. Execute o cliente:
```bash
java ChatClientGUI
```

3. Na interface do cliente:
   - Digite seu nome de usuário
   - Para conexão local, use "localhost" como endereço do servidor
   - A porta padrão é 4444
   - Clique em "Conectar"

## Conexão Remota

Para conectar de outra máquina:

1. No servidor:
   - Descubra seu IP público (use sites como whatismyip.com)
   - Configure o redirecionamento de porta no seu roteador:
     - Porta externa: 4444
     - Porta interna: 4444
     - Protocolo: TCP
   - Certifique-se que o firewall permite conexões na porta 4444

2. No cliente:
   - Use o IP público do servidor como endereço
   - Use a porta 4444
   - Digite seu nome de usuário
   - Clique em "Conectar"

## Estrutura do Projeto

- `ChatServerGUI.java`: Interface gráfica do servidor
- `ChatClientGUI.java`: Interface gráfica do cliente
- `Servidor.java`: Implementação do servidor TCP
- `Cliente.java`: Implementação do cliente TCP

## Comandos do Chat

- Para enviar uma mensagem: Digite o texto e pressione Enter ou clique em "Enviar"
- Para desconectar: Clique em "Desconectar" no cliente
- Para parar o servidor: Clique em "Parar Servidor" no servidor

## Observações

- O servidor deve ser iniciado antes dos clientes
- Cada cliente deve usar um nome de usuário único
- As mensagens são enviadas em tempo real para todos os usuários conectados
- O sistema notifica quando usuários entram ou saem do chat

## Solução de Problemas

1. Se não conseguir conectar:
   - Verifique se o servidor está rodando
   - Confirme se o endereço e porta estão corretos
   - Verifique as configurações de firewall
   - Para conexão remota, confirme o redirecionamento de porta

2. Se o servidor não iniciar:
   - Verifique se a porta 4444 não está em uso
   - Confirme as permissões de firewall
   - Tente usar uma porta diferente (requer alteração no código)

## Contribuição

Este projeto foi desenvolvido como parte de um laboratório de redes. Contribuições são bem-vindas através de pull requests.
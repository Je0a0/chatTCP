# Chat TCP com Interface Gráfica

Este é um sistema de chat em tempo real desenvolvido em Java, utilizando sockets TCP e interface gráfica Swing. O sistema permite comunicação entre múltiplos usuários através de um servidor central.

## Funcionalidades

- Interface gráfica para servidor e cliente
- Suporte a múltiplos usuários simultâneos
- Notificações de entrada e saída de usuários
- Mensagens em tempo real
- Conexão local e remota
- Configuração flexível de IP e porta

## Requisitos

- Java 8 ou superior
- Sistema operacional com suporte a Java

## Como Executar

### Servidor

1. Compile o servidor:
```bash
javac src/ChatServerGUI.java
```

2. Execute o servidor:
```bash
java -cp src ChatServerGUI
```

3. Na interface do servidor:
   - Digite o IP do servidor (use "localhost" para conexões locais)
   - Digite a porta desejada (padrão: 4444)
   - Clique em "Iniciar Servidor"

### Cliente

1. Compile o cliente:
```bash
javac src/ChatClientGUI.java
```

2. Execute o cliente:
```bash
java -cp src ChatClientGUI
```

3. Na interface do cliente:
   - Digite seu nome de usuário
   - Para conexão local, use "localhost" como endereço do servidor
   - Use a mesma porta configurada no servidor
   - Clique em "Conectar"

## Conexão Remota

Para conectar de outra máquina:

1. No servidor:
   - Descubra seu IP público:
     - No Windows: Abra o cmd e digite `ipconfig`
     - No Linux/Mac: Abra o terminal e digite `ifconfig` ou `ip addr`
     - Ou use sites como whatismyip.com
   - Digite o IP público do servidor no campo IP
   - Configure a porta desejada (ex: 4444)
   - Clique em "Iniciar Servidor"
   - Configure o redirecionamento de porta no seu roteador:
     - Acesse a interface do roteador (geralmente 192.168.0.1 ou 192.168.1.1)
     - Procure por "Port Forwarding" ou "Redirecionamento de Porta"
     - Adicione uma nova regra:
       - Nome: Chat TCP
       - Porta externa: mesma porta configurada no servidor
       - Porta interna: mesma porta configurada no servidor
       - Protocolo: TCP
       - IP interno: IP local do seu computador
   - Configure o firewall do Windows:
     - Abra o "Firewall do Windows Defender"
     - Clique em "Regras de Entrada"
     - Clique em "Nova Regra"
     - Selecione "Porta"
     - Selecione "TCP" e digite a porta configurada
     - Selecione "Permitir a conexão"
     - Marque todos os perfis
     - Dê um nome (ex: "Chat TCP") e finalize

2. No cliente:
   - Use o IP público do servidor como endereço
   - Use a mesma porta configurada no servidor
   - Digite seu nome de usuário
   - Clique em "Conectar"

### Solução de Problemas de Conexão Remota

1. Se não conseguir conectar:
   - Verifique se o servidor está rodando
   - Confirme se o IP público está correto
   - Teste se a porta está aberta:
     - No Windows: `netstat -an | findstr "4444"`
     - No Linux/Mac: `netstat -an | grep "4444"`
   - Verifique se o redirecionamento de porta está configurado corretamente
   - Teste se o firewall está permitindo conexões
   - Tente desativar temporariamente o firewall para teste
   - Verifique se o antivírus não está bloqueando a conexão

2. Para testar se a porta está acessível:
   - Use ferramentas online como portscanner.co
   - Ou use o comando telnet:
     - No Windows: `telnet seu_ip_publico 4444`
     - No Linux/Mac: `nc -zv seu_ip_publico 4444`

## Estrutura do Projeto

```
chatTCP/
├── src/
│   ├── ChatServerGUI.java
│   ├── ChatClientGUI.java
│   ├── Servidor.java
│   └── Cliente.java
├── out/
│   └── production/
│       └── chatTCP/
└── README.md
```

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
   - Verifique se a porta configurada não está em uso
   - Confirme as permissões de firewall
   - Tente usar uma porta diferente

## Contribuição

Este projeto foi desenvolvido como parte de um laboratório de redes. Contribuições são bem-vindas através de pull requests.
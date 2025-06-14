# Chat Multi-usuários

Um aplicativo de chat multi-usuários com interface gráfica moderna desenvolvido em Java usando Swing.

## Requisitos

- Java 11 ou superior
- Maven

## Estrutura do Projeto

```
chat-multiusuario/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── chat/
│                   ├── ChatClient.java    # Cliente com interface Swing
│                   └── Servidor.java      # Servidor do chat
└── pom.xml                                # Configuração do Maven
```

## Como Executar

1. Clone o repositório
2. Abra um terminal na pasta do projeto
3. Execute o servidor:
   ```bash
   mvn exec:java -Dexec.mainClass="com.chat.Servidor"
   ```
4. Execute o cliente:
   ```bash
   mvn exec:java -Dexec.mainClass="com.chat.ChatClient"
   ```

## Funcionalidades

- Interface gráfica moderna e responsiva usando Swing
- Conexão com servidor TCP
- Chat em tempo real
- Suporte a múltiplos usuários
- Notificações de entrada e saída de usuários
- Design moderno com cores e estilos atualizados

## Tecnologias Utilizadas

- Java 11
- Swing para interface gráfica
- Maven 
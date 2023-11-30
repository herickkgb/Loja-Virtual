**Boas-Vindas aos Amantes da Moda e Usuários Empolgados!**

# imagem do app
<div align="center">
  <img src="app/src/main/res/drawable/imagensApp/imglogin.png?raw=true" width="150">
  <img src="app/src/main/res/drawable/imagensApp/imgCadastro.png?raw=true" width="150">
  <img src="app/src/main/res/drawable/imagensApp/imgprincipal.png?raw=true" width="150">
  <img src="app/src/main/res/drawable/imagensApp/imgDetalhes.png?raw=true" width="150">
  <img src="app/src/main/res/drawable/imagensApp/imgDetalhesProduto.png?raw=true" width="150">
  <img src="app/src/main/res/drawable/imagensApp/imgEndereco.png?raw=true" width="150">
  <img src="app/src/main/res/drawable/imagensApp/imgPagamento.png?raw=true" width="150">
   <img src="app/src/main/res/drawable/imagensApp/imgPedidosEntrega.png?raw=true" width="150">
 

</div>

É com grande entusiasmo que compartilho com vocês o aplicativo de loja virtual de sapatos que desenvolvi como parte do meu aprendizado em programação. Este projeto foi criado com o objetivo de proporcionar a vocês uma experiência de compras única, pensada para atender às suas necessidades de estilo com facilidade e conveniência. Vamos mergulhar nas tecnologias-chave que eu escolhi para impulsionar esta aplicação e garantir uma experiência de usuário excepcional.

**1. Retrofit - Conectando Você ao Mundo da Moda:**
   - O Retrofit foi utilizado para simplificar a comunicação com APIs, assegurando uma conexão eficiente e rápida com nosso servidor.
   - Dependências:
     ```kotlin
     implementation("com.squareup.retrofit2:retrofit:2.9.0")
     implementation("com.squareup.retrofit2:converter-gson:2.9.0")
     implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
     ```

**2. Gson - Transformando Dados em Estilo:**
   - Integrei a biblioteca Gson para facilitar a serialização e desserialização de dados, garantindo uma comunicação suave entre o aplicativo e o servidor.
   - Dependência:
     ```kotlin
     implementation("com.google.code.gson:gson:2.9.0")
     ```

**3. Lottie - Toque de Magia com Animações:**
   - Adicionei o Lottie para proporcionar animações envolventes, tornando a experiência de compra ainda mais atraente.
   - Dependência:
     ```kotlin
     implementation("com.airbnb.android:lottie:6.2.0")
     ```

**4. Firebase - Garantindo Segurança e Eficiência:**
   - Utilizei o Firebase para recursos essenciais, incluindo autenticação segura, armazenamento de dados em tempo real e armazenamento de arquivos.
   - Dependência:
     ```kotlin
     implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
     implementation("com.google.firebase:firebase-auth-ktx")
     implementation("com.google.firebase:firebase-firestore-ktx")
     implementation("com.google.firebase:firebase-storage-ktx")
     ```

**5. Google Play Services - Autenticação Robusta:**
   - Adotei `play-services-auth` para garantir uma autenticação sólida e segura.
   - Dependência:
     ```kotlin
     implementation("com.google.android.gms:play-services-auth:20.7.0")
     ```

**6. CircleImageView - Detalhes com Estilo:**
   - Integrei a biblioteca CircleImageView para exibir imagens de perfil com elegância circular.
   - Dependência:
     ```kotlin
     implementation("de.hdodenhof:circleimageview:3.1.0")
     ```

**7. Mercado Pago - Facilitando Pagamentos:**
   - Incorporamos o Mercado Pago para oferecer opções de pagamento seguras e diversificadas.
   - Dependência:
     ```kotlin
     implementation("com.mercadopago.android.px:checkout:4.+")
     ```

Este projeto representa meu esforço e dedicação enquanto aprendo. Se tiverem dúvidas ou sugestões, por favor, entrem em contato. Este aplicativo é feito com carinho e pensado para vocês. 

Agradeço por contribuírem para o crescimento deste projeto!

**Obrigado pela oportunidade de compartilhar minha jornada de aprendizado com vocês! 🚀👟✨**

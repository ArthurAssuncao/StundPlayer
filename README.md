#Stund Player
============

O **Stund Player** é um software open-source desenvolvido em Java, com a finalizade de ser um player nas nuvens, tendo um cliente e um servidor(onde as músicas são armazenadas).<br />
Foi desenvolvido como trabalho para a disciplina de Redes de Computadores do 3º período do curso Tecnologia em Sistemas para Internet, apenas para fins acadêmicos.

Utiliza o protocolo Real Time Streaming Protocol (RTSP) para o controle na transferência dos dados e o protocolo Real-time Transport Protocol (RTP) para a entrega dos dados fragmentados do arquivo de áudio MP3.
Permite, além das funções essenciais a qualquer player, o envio de músicas para o servidor.


##Leia-me
###Cliente
####Como utilizar
* De duplo clique no cliente ou abra pelo terminal "java -jar cliente.jar".
* Vá na ferramenta localizada no canto superior direito do cliente e configure o IP do servidor, se não tem um servidor, faça o download do servidor em: http://code.google.com/p/servidor
* Crie um conta colocando o nome de usuário e uma senha.
* Desfrute do player.

#####Na interface do player
* Para enviar uma música basta:
	* clicar na playlist e apertar o botão INSERT.
	* clicar no botão com símbolo de ejetar (sexto botão laranja).
	* clicar no nome de usuário e na opção "enviar música".

* Para ouvir uma música
	* de duplo clique numa música

* Para tocar todas as músicas, uma após a outra
	* clique no botão "repeat" acima da barra de volume, por padrão ele fica como "repetir[ON]" que significa repetir a própria música, ao clicar ficará como "repetir[TUDO]", onde repete todas as músicas do usuário.


###Servidor
#####Como utilizar
* De duplo clique no servidor ou abra pelo terminal "java -jar servidor.jar", de preferência use pelo terminal.
* Pronto, agora apenas espere por conexões de clientes, se não tem um cliente, faça o download em: http://code.google.com/p/cliente


##Tecnologias utilizadas
* Linguagem de programação Java
* Banco de Dados HSQLDB (HyperSQL DataBase)
* Bibliotecas
  * JLayer - execução dos áudios
  * MP3 SPI - para obter os metadados com as informações dos áudios MP3.
  * SwingX - extensão de componentes swing
  * JLayerPausable - conjunto de classes que adicionam a função de pause ao JLayer, mais informações em [ThisCouldBeBetter](http://thiscouldbebetter.wordpress.com/2011/07/04/pausing-an-mp3-file-using-jlayer/)


##Screnshots

![Stund Player](https://github.com/ArthurAssuncao/StundPlayer/raw/master/screenshots/preview1.0.png)

Agradecimentos especiais a (Special thanks to):

* James F. Kurose & Keith W. Ross (Computer Networking, 5 ed)
* Paul Deitel & Harvey Deitel (Java How to Program, 8 ed)
* http://www.csee.umbc.edu/~pmundur/courses/CMSC691C/lab5-kurose-ross.html
* http://thiscouldbebetter.wordpress.com/2011/07/04/pausing-an-mp3-file-using-jlayer/
* E aos meus professores 

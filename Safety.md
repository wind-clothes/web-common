## Safety
概述：

级别：

* 明文传输
* 明文传输，增加签名机制，可以保证数据的唯一性，完整性和正确性，无法对数据加密和防劫持
* https和签名机制，可以保证数据的安全性，唯一性，完整性，正确性。

## OAuth2.0
概述：
OAuth的参与实体至少有如下三个：
* RO (resource owner): 资源所有者，对资源具有授权能力的人。如上文中的用户Alice。
* RS (resource server): 资源服务器，它存储资源，并处理对资源的访问请求。如Google资源服务器，它所保管的资源就是用户Alice的照片。
* Client: 第三方应用，它获得RO的授权后便可以去访问RO的资源。如网易印像服务。
此外，为了支持开放授权功能以及更好地描述开放授权协议，OAuth引入了第四个参与实体：
* AS (authorization server): 授权服务器，它认证RO的身份，为RO提供授权审批流程，并最终颁发授权令牌(Access Token)。读者请注意，为了便于协议的描述，这里只是在逻辑上把AS与RS区分开来；在物理上，AS与RS的功能可以由同一个服务器来提供服务。

### 认证模式

客户端必须得到用户的授权（authorization grant），才能获得令牌（access token）。OAuth 2.0定义了四种授权方式。
* 授权码模式（authorization code）
* 简化模式（implicit）
* 密码模式（resource owner password credentials）
* 客户端模式（client credentials）

OAuth 2.0中的Authorization Grant代表一种中间凭证（Intermediate Credential），它代表了资源拥有者针对客户端应用获取目标资源的授权。OAuth 2.0定义了如下4种Authorization Grant类型，我们也可以利用定义其中的扩展机制定制其他类型的Authorization Grant。Authorization Grant的类型体现了授权采用的方式以及Access Token的获取机制。

* Implicit：它代表一种“隐式”授权方式，即客户端在取得资源拥有者（最终用户）授权的情况下直接获取Access Token，而不是间接地利用获取的Authorization Grant来取得Access Token。换句话说，此种类型的Authorization Grant代表根本不需要Authorization Grant，那么上面介绍的“Three-Legged OAuth”变成了“Two-Legged OAuth”。
* Authorization Code：这是最为典型的Authorization Grant，客户端应用在取得资源拥有者授权之后会从授权服务器得到一个Authorization Code作为Authorization Grant。在它获取寄宿于资源服务器中的目标资源之前，需要利用此Authorization Code从授权服务器获取Access Token。
* Resource Owner Password Credentials：资源拥有者的凭证直接作为获取Access Token的Authorization Grant。这种Authorization Grant类型貌似与OAuth设计的初衷向违背（OAuth的主要目的在于让客户端应用在不需要提供资源拥有者凭证的情况下能够以他的名义获取受保护的资源），但是如果客户端程序是值得被信任的，用户（资源拥有者）向其提供自己的凭证也是可以接受的。
* Client Credentials：客户端应用自身的凭证直接作为它用于获取Access Token的Authorization Grant。这种类型的Authorization Grant适用于客户端应用获取属于自己的资源，换句话说客户端应用本身相当于资源的拥有者。

https://yq.aliyun.com/articles/226785?spm=5176.100239.blogcont226788.27.ot3cYW

https://yq.aliyun.com/articles/226788?spm=5176.100239.blogcont226789.23.PE6Sfu

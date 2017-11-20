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

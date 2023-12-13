## REST API 기반 안드로이드 앱 샘플
- **Anroid-RestAPI** 안드로이드 프로젝트는 **Amazon API Gateway**를 통해서 게시된 REST API를 활용하여 **IoT 벡엔드**와 상호작용하는 안드로이드 앱 샘플입니다. 안드로이드 앱의 주요 기능은 다음과 같습니다.
	- 디바이스 목록 조회 
	- 디바이스 상태 조회/변경
	- 디바이스 로그 조회
- **IoT 백엔드**는 AWS의 다양한 서비스(AWS IoT Core, AWS Lambda, Amazon DyanmoDB, Amazon SNS, Amazon API Gateway)를 이용해 구축된 IoT 클라우드 플랫폼입니다.
	- **AWS IoT Device gateway**를 통해서 연결된 **아두이노 디바이스**(**MKRWiFi1010**)로부터 주기적으로 매 5초마다 디바이스 정보(현재 온도 및 LED 상태)를 수신하고, MQTT 프로토콜을 이용하여 **Device shadow** 혹은 **IoT rule** 컴포넌트와 상호작용합니다.
		- **Device shadow**는 **Device gateway**를 통해 게시된 주제에 따라 디바이스 상태정보를 업데이트하거나 현재 상태정보를 게시합니다.
		- **IoT rule**은 등록된 주제(update/documents)의 메시지가 수신될 때마다 **AWS Lambda** 함수를 통해서 수신된 메시지를 **Amazon DynamoDB**에 저장하거나, 수신된 메시지를 분석하여 특정 조건이 만족되면 경고 메시지를 **Amazon SNS**의 주제에 게시하여 해당 주제에 등록된 이메일 계정으로 경고 메시지가 전달되게 한다.
	- **Amazon API Gateway**는 **AWS Lambda**함수를 통해서 다음 기능을 수행하는 두 가지 REST API를 게시한다.
		- AWS IoT Core에 등록된 디바이스 목록을 조회하는 REST API
		- 디바이스의 정보를 변경하거나 조회하는 REST API
		- 디바이스의 로그 정보를 조회하는 REST API

### 1. REST API 설계
- 다음 프로그램 코드를 실행시키기 위해서는 다음 형식의 **REST API**가 준비되어 있어야 합니다.
	- 디바이스 목록 조회
		
		```
		GET /devices
		```
	
	- 디바이스 상태 조회

		```	
		GET /devices/{deviceId}
		```
	- 디바이스 상태 변경

		```	
		PUT /devices/{deviceId}
		```
		
		- message body (payload)
		
			```json
			{ 
				"tags" : [
					{
						"attrName": "temperature",
						"attrValue": "27.0"
					},
					{
						"attrName": "LED",
						"attrValue": "OFF"
					}
				]
			}

	
	- 디바이스 로그 조회	

		```		
		GET /devices/{deviceId}/log?from=yyyy-mm-dd hh:mm:ss&to=yyyy-mm-dd hh:mm:ss
		```
		
### 2. AWS IoT 백엔드 구축

다음 방법을 통해서 다음 IoT 백엔드를 구축한다.

1. [AWS IoT로 DHT-11 센서 및 LED 제어](https://kwanulee.github.io/AWS_IoT_DHT11/)
2. [AWS Lambda를 이용한 Device Monitoring 실습](https://kwanulee.github.io/DeviceMonitoringWithLambda/)
3. [DynamoDB를 이용한 백엔드 구축하기](https://kwanulee.github.io/DynamoDB/)
4. [API Gateway를 통한 Device Shadow 액세스 하기](https://kwanulee.github.io/APIG송

		

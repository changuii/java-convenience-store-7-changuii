# java-convenience-store-precourse

### 프로젝트 소개

> 편의점에서 상품을 구매할 수 있는 편의점 애플리케이션입니다.
>

### 기능

1. 편의점의 상품 목록들을 한눈에 볼 수 있습니다.
    1. 상품 이름, 상품 가격, 상품 현재 재고, 상품 프로모션에 대한 정보를 확인할 수 있습니다.
2. 입력하신 상품명과 수량에 맞게 상품을 구입할 수 있습니다.
3. 만약 프로모션이 적용안되는 상품이 있거나 프로모션이 적용되지만 부족하게 가져오신 경우 저희가 고지해드립니다.
4. 원하시는 경우, 멤버십 할인도 받을 수 있습니다.
5. 구매하신 내역은 영수증을 통해 확인하실 수 있습니다.
    1. 영수증에는 구매 상품 내역, 각 상품 가격, 총 구입 금액, 프로모션 할인 금액, 멤버십 할인 금액, 총 내야하실 금액 등이 표기됩니다.
6. 이어서 구매하시고 싶으시다면 계속 하실 수 있습니다.

---

### 기능 예제

**1. 편의점의 상품 목록들을 확인**

- 아래 품목들은 기본 품목들로 초기 설정입니다. 각 상품의 프로모션 상품 정보, 일반 상품 정보 순으로 나타납니다.
    - 프로모션이 없는 상품은 표기되지 않습니다.
    - 일반 상품은 재고가 없더라도 정보는 표기됩니다.

```
안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 10개 탄산2+1
- 콜라 1,000원 10개 
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개 
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음 
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음 
- 물 500원 10개 
- 비타민워터 1,500원 6개 
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개 
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개 
- 에너지바 2,000원 5개 
- 정식도시락 6,400원 8개 
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개 
```

**2. 상품 구입**

- 아래의 예시는 콜라 1개와 물 5개를 구매하는 예제입니다.
    - 여러개의 상품을 구매하는 것은 가능하나, 중복된 상품을 여러개 입력하시는 것은 불가능합니다.

```
구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[콜라-1],[물-5]
```

**3. 만약 프로모션이 적용되지만 부족하게 가져오신 경우 고지해드립니다.**

- 콜라 제품은 2+1 행사중이기 때문에 1개를 부족하게 가져오신 예제입니다.
    - Y를 입력하시면 1개를 무료 증정받을 수 있습니다.
    - N을 입력하시면 무료 증정을 거부하실 수 있습니다.

```
구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[콜라-2]

현재 콜라은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
Y
```

**4. 만약 프로모션을 제공받지 못하는 수량이 있는 경우 고지해드립니다.**

- 컵라면 제품은 1+1 행사중이기 때문에 1개를 추가로 받으셔야 하지만, 수량이 부족한 관계로 혜택을 받지 못하시는 예제입니다.
    - Y를 입력하시면 적용되지 않는 수량을 제거하고 구매하실 수 있습니다.
    - N을 입력하시면 프로모션 혜택이 없는 정가로 구매하실 수 있습니다.

```
- 컵라면 1,700원 1개 MD추천상품

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[컵라면-1]

현재 컵라면 1개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
Y
```

**5. 원하시는 경우 정가 구매 항목에 대해서 멤버십 할인 30%를 적용해드립니다. (최대 8000원)**

- 아래는 콜라를 제외한 물 10개에 멤버십 할인 30%가 적용된 예제입니다.

```
구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[콜라-3],[물-10]

멤버십 할인을 받으시겠습니까? (Y/N)
Y
==============W 편의점================
상품명						수량			     금액
콜라							3			       3,000
물				  			10			     5,000
=============증		정===============
콜라							1			
====================================
총구매액					13			     8,000
행사할인						          -1,000
멤버십할인						          -1,500
내실돈					     	         5,500
```

**6. 구매하신 내역은 영수증을 통해 확인하실 수 있습니다.**

- 아래 예시는 모든 상품을 구매한 예제입니다.

```
==============W 편의점================
상품명						수량					금액
콜라							20					20,000
사이다						15					15,000
오렌지주스					9					  16,200
탄산수						5					  6,000
물				  			10					5,000
비타민워터					6			  		9,000
감자칩						10					15,000
초코바						10					12,000
에너지바					5			  		10,000
정식도시락					8			  		51,200
컵라면						11					18,700
=============증		정===============
콜라							3			
사이다						2			
오렌지주스					4			
탄산수						1			
감자칩						2			
초코바						2			
====================================
총구매액					109				178,100
행사할인										-18,800
멤버십할인										-8,000
내실돈								 		  151,300
```

**7. 이어서 구매하시고 싶으신 경우 이어서 진행할 수 있습니다.**

- 이전 구매에서 콜라3개를 구매하여 3개가 차감되어 있습니다.

```
감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
Y

안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 7개 탄산2+1
- 콜라 1,000원 10개 
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개 
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음 
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음 
- 물 500원 10개 
- 비타민워터 1,500원 6개 
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개 
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개 
- 에너지바 2,000원 5개 
- 정식도시락 6,400원 8개 
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개 

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
```

### 입력 가이드

**1. 물품 입력 가이드**

- 물품 입력은 아래와 같은 형식으로 입력해주셔야 합니다.
- [`상품이름`-`구매하시려는 수량`],[`상품이름`-`구매하시려는 수량`],[`상품이름`-`구매하시려는 수량`]
- 구매하시는 상품을 추가하는 것은 제한이 없지만, 같은 상품에 대해서 두 번 입력하시는 것은 불가능합니다.

```
구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[사이다-2],[감자칩-1],[오렌지주스-2]
```

**2. 프로모션 상품 추가 및 프로모션 미 적용 상품 제외, 멤버십 할인 및 재 구매 의사 입력 가이드**

- 아래의 입력들은 오직 `Y` 혹은 `N` 만 입력하실 수 있습니다.

```
현재 콜라은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
현재 컵라면 1개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
멤버십 할인을 받으시겠습니까? (Y/N)
감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
```

### 프로젝트 패키지 구조

**Class Diagram**

```mermaid
classDiagram
direction BT
class Bill
class BuyGet
class CompletedPurchaseHistory
class Consumer
class ConvenienceStoreService
class DateRange
class InputParser
class InputValidator
class InputView
class OutputView
class Product
class ProductInventory
class ProductQuantity
class Promotion
class PromotionProductQuantity
class PurchaseHistory
class PurchaseProduct
class StoreController

Bill "1" *--> "completedPurchaseHistories *" CompletedPurchaseHistory 
Consumer "1" *--> "completedPurchaseHistories *" CompletedPurchaseHistory 
Consumer "1" *--> "purchaseProducts *" PurchaseProduct 
ConvenienceStoreService "1" *--> "productInventory 1" ProductInventory 
InputView "1" *--> "inputParser 1" InputParser 
InputView "1" *--> "inputValidator 1" InputValidator 
ProductInventory "1" *--> "infos *" Product 
ProductInventory "1" *--> "quantities *" ProductQuantity 
ProductInventory "1" *--> "promotionQuantities *" PromotionProductQuantity 
Promotion "1" *--> "buyGet 1" BuyGet 
Promotion "1" *--> "dateRange 1" DateRange 
PromotionProductQuantity "1" *--> "promotion 1" Promotion 
PurchaseProduct "1" *--> "purchaseHistory 1" PurchaseHistory 
StoreController "1" *--> "convenienceStoreService 1" ConvenienceStoreService 
StoreController "1" *--> "inputView 1" InputView 
StoreController "1" *--> "outputView 1" OutputView 

```

**패키지 트리 구조**

```
store
	├── Application.java
	├── component
	│   ├── DTOConverter.java
	│   ├── FileParser.java
	│   └── generator
	│       ├── ConsumerGenerator.java
	│       ├── ConvenienceStoreServiceGnerator.java
	│       ├── LocalDateGenerator.java
	│       ├── ProductInventoryGenerator.java
	│       ├── PromotionGenerator.java
	│       └── TodayGenerator.java
	├── controller
	│   └── StoreController.java
	├── domain
	│   ├── Bill.java
	│   ├── CompletedPurchaseHistory.java
	│   ├── Consumer.java
	│   ├── ProductInventory.java
	│   ├── PurchaseHistory.java
	│   ├── PurchaseProduct.java
	│   └── product
	│       ├── BuyGet.java
	│       ├── DateRange.java
	│       ├── Product.java
	│       ├── ProductQuantity.java
	│       ├── Promotion.java
	│       └── PromotionProductQuantity.java
	├── dto
	│   ├── BillDTO.java
	│   ├── ProductDTO.java
	│   ├── ProductInventoryDTO.java
	│   └── PurchaseProductDTO.java
	├── enums
	│   ├── ErrorMessage.java
	│   ├── GeneratorConstants.java
	│   └── OutputMessage.java
	├── factory
	│   └── StoreControllerFactory.java
	├── handler
	│   └── RetryHandler.java
	├── service
	│   └── ConvenienceStoreService.java
	└── view
	    ├── InputParser.java
	    ├── InputValidator.java
	    ├── InputView.java
	    └── OutputView.java
```


## 기능 목록

### 입력

- [x]  구매할 상품과 수량을 입력받는다.
    - [x]  상품명, 수량은 하이픈(-)으로, 개별 상품은 대괄호([])로 묶어 쉼표(,)로 구분한다.
    - 예시 : [콜라-10],[사이다-3]
- [x]  Y 또는 N를 입력받는다.
    - [x]  그 외의 입력은 모두 예외 상황으로 처리한다.

### 출력

- [x]  환영 인사를 출력한다. 아래와 같다.

```plaintext
안녕하세요. W편의점입니다.
```

- [x]  상품명, 가격, 프로모션 이름, 재고를 안내한다.
    - [x]  재고가 0개인 경우 [재고 없음]으로 출력한다.

```plaintext
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 10개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 5개
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개
```
- [x]  사용자에게 구매할 상품명과 수량을 입력해달라는 메시지를 출력한다.

```plaintext
구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
```

- [x]  프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우, 혜택에 대한 안내 메시지를 출력한다.

```plaintext
현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
```

- [x]  프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부에 대한 안내 메시지를 출력한다.

```plaintext
현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
```

- [x]  멤버십 할인 적용 여부를 확인하기 위해 안내 문구를 출력한다.

```plaintext
멤버십 할인을 받으시겠습니까? (Y/N)
```

- [x]  구매 상품 내역, 증정 상품 내역, 금액 정보를 출력한다.
    - [x]  영수증의 구성 요소를 보기 좋게 정렬하여 고객이 쉽게 금액과 수량을 확인할 수 있게 한다.
    - [x]  구매 상품 내역: 구매한 상품명, 수량, 가격
    - [x]  증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록
    - [x]  금액 정보
        - [x]  총구매액: 구매한 상품의 총 수량과 총 금액
        - [x]  행사할인: 프로모션에 의해 할인된 금액
        - [x]  멤버십할인: 멤버십에 의해 추가로 할인된 금액
        - [x]  내실돈: 최종 결제 금액

```plaintext
===========W 편의점=============
상품명		        수량	금액
콜라		             3 	3,000
에너지바 	          	 5 	10,000
===========증	정=============
콜라		             1
==============================
총구매액		         8	13,000
행사할인			        -1,000
멤버십할인			        -3,000
내실돈			         9,000
```

- [x]  추가 구매 여부를 확인하기 위해 안내 문구를 출력한다.

```plaintext
감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
```

- [x]  사용자가 잘못된 값을 입력한 경우, “[ERROR]”로 시작하는 오류 메시지와 함께 상황에 맞는 안내를 출력한다.
    - [x]  구매할 상품과 수량 형식이 올바르지 않은 경우: `[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.`
    - [x]  존재하지 않는 상품을 입력한 경우: `[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.`
    - [x]  구매 수량이 재고 수량을 초과한 경우: `[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.`
    - [x]  기타 잘못된 입력의 경우: `[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`

### 기능

- [x]  편의점 재고를 생성한다.
    - [x]  구현에 필요한 상품 목록과 행사 목록을 파일 입출력을 통해 불러온다.
        - [x]  `src/main/resources/products.md`과 `src/main/resources/promotions.md` 파일을 이용한다.
        - [x]  두 파일 모두 내용의 형식을 유지한다면 값은 수정할 수 있다.
- [x]  재고 관리
    - [x]  각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인한다.
    - [x]  고객이 상품을 구매할 때, 결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 관리한다.
- [x]  프로모션 할인
    - [x]  오늘 날짜가 프로모션 기간 내에 포함된 경우에만 할인을 적용한다.
    - [x]  프로모션은  N개 구매 시 1개 무료 증정의 형태로 진행된다.
    - [x]  동일 상품에 여러 프로모션이 적용되지 않는다.
    - [x]  프로모션 기간인 경우 프로모션 재고를 우선적으로 차감하고, 프로모션 재고가 부족한 경우 일반 재고를 사용한다.
    - [x]  프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받는다. 입력은 다음과 같다.
        - [x]  Y : 증정 받는 상품을 추가한다.
        - [x]  N : 증정 받는 상품을 추가하지 않는다.
    - [x]  프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력받는다. 입력은 다음과 같다.
        - [x]  Y : 일부 수량에 대해 정가로 결제
        - [x]  N : 정가로 결제해야하는 수량만큼 제외한 후 결제를 진행
- [x]  멤버십 할인
    - [x]  프로모션 미적용 금액의 30%를 할인받는다.
    - [x]  프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용한다.
    - [x]  최대 한도는 8000원이다.
    - [x]  멤버십 할인 적용 여부를 입력받는다. 입력은 다음과 같다.
        - [x]  Y : 멤버십 할인을 적용한다.
        - [x]  N : 멤버십 할인을 적용하지 않는다.
- [x]  전체 로직
    - [x]  추가 구매 여부를 입력받는다.
        - [x]  Y : 재고가 업데이트된 상품 목록을 확인 후 추가로 구매를 진행한다.
        - [x]  N : 구매를 종료한다.

### 실행 흐름

1. 사용자에게 편의점 소개를 출력한다.
2. 사용자에게 편의점 재고 현황을 출력한다.
3. 사용자에게 구매할 상품명과 수량을 입력받기 위한 메시지를 출력한다.
4. 사용자에게 구매할 상품명과 수량을 입력받는다.
5. 사용자에게 입력받은 구매할 상품명과 수량을 검증한다.
    1. 입력에 대한 형식을 검증
    2. 상품의 존재 여부를 검증
    3. 상품의 수량이 구매가능한지 검증
    4. 프로모션 적용 상품의 개수가 혜택 수량만큼 가져오지 않은지 검증
    5. 프로모션 적용 상품의 재고가 부족한지 검증
6. 만약 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않은 경우, 혜택에 대한 안내 메시지를 출력한다.
7. 혜택에 대한 사용자의 응답을 입력받는다.
    1. Y : 구매 수량에 해당 프로모션 상품을 1개 추가한다.
    2. N : 구매 수량을 그대로 진행한다.
8. 만약 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부에 대한 안내 메시지를 출력한다.
9. 혜택에 대한 사용자의 응답을 입력받는다.
    1. Y : 구매 수량을 그대로 진행한다.
    2. N : 구매 수량에서 해당 프로모션을 제거한다.
10. 사용자에게 멤버십  할인 적용 여부를 확인하기 위해 안내 문구를 출력한다.
11. 멤버십 할인에 대한 사용자의 응답을 입력받는다.
    1. Y : 멤버십 할인을 적용한다.
    2. N : 멤버십 할인을 적용하지 않는다.
12. 사용자가 구매한 내역에 대해서 재고를 처리한다.
13. 사용자의 입력에 대한 구매 상품 내역, 증정 상품 내역, 금액 정보를 출력한다.
14. 사용자에게 구매 여부를 확인하기 위해 안내 문구를 출력한다.
15. 사용자의 구매 여부를 입력받는다.
    1. Y : 1번부터 다시 시작
    2. N : 애플리케이션 종료

### 예외 상황

InputValidator

- [x]  PurchaseProducts
    - [x]  형식을 올바르게 지키고 않는 경우 (올바른 경우 : [상품명-수량],[상품명-수량] )
- [x]  List<PurchaseProductsDTOs>
    - [x]  같은 이름의 구매 물품이 있는지 여부
    - [x]  구매 수량이 0개 이하인 경우
- [x]  answer
    - [x]  Y 혹은 N 이외의 경우에는 예외가 발생한다.

PromotionGenerator

- [x] 파일의 형식을 지키지 않은 경우
  - [x] 날짜의 형식이 잘못된 경우
  - [x] 프로모션의 형식이 잘못된 경우
  - [x] 이벤트 수량이 int type의 범위를 벗어난 경우

ProductGenerator
- [x] 파일의 형식을 지키지 않은 경우
  - [x] 상품 형식이 잘못된 경우
  - [x] 재고 수량이 int type의 범위를 벗어난 경우
  - [x] 상품 가격이 int type의 범위를 벗어난 경우
- [x] 존재하지 않는 Promotion을 가진 경우
   
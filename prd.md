## <u> The Errand Company </u>
### Product Requirements Description

### 1. <u>USP of The Errand Co:</u>

The Errand Company is an e-commerce platform that aims to provide any and all services to its customers through customized ordering and personalized customer service. Orders are fulfilled within 24 hours through a logistics framework that sources products from the nearest local store and delivers it to the customer.

### <u>2. Customer App: </u>

The customer app will consist of a home page that will describe the latest deals, most recent products and offers, and also allow easy access to purchase and checkout items. 

#### A. Have a floating shop button:
Since our app does not catalogue specific products, it would be better to have a floating cart on all the app activities so that the user could instantly place orders from wherever he/she is in the app. 

> 1. Use fragment activity to make the floating button
> 2. Each order should consist of a title and an optional order description
> 3. Add button within the shop fragment to add items
> 4. Checkout button to choose payment option and place order
> 5. Choose from list of saved addresses or create new address

#### B. Customer chat service: 

> 1. Connect with customer representative, allow calls to place order, inquire about products, etc. 

#### C. Account fragment activity: 
> 1. Change account details: phone number, address, name, email
> 2. Raise customer service tickets
> 3. Submit feedback
> 4. View past order history and status
> 5. Track present order status

#### D. Login activity: 
> 1. Use present splash screen to lead to sign-in or sign-up [DONE]
> 2. Use google auth to verify email, phone, and sign-in [DONE]
> 3. Locate customers (g-maps) to use as default present delivery address


### 3. <u> Front-end Customer website: </u>
> 1. Implement same features as app for customer facing side
> 2. Implement admin panel for front-end management of DB

### 4. <u>Backend:</u>
> 1. Create new user and update DB instantly once sign-in success
> 2. Load customer details on sign-in and populate UI with other stuff like products, offers, etc
> 3. Upon order/feedback submission update DB and start tracking of order, payment submission, issue status
> 4. Maintain records of each customer: number of orders, order addresses, chat history, call history, feedback history, issue history, payment history. 
> 5. Update admin on receiving order, distribute order to delivery boys using load-sharing algorithm, distribute and assign orders using Telegram. 

### Platform Services:
The following lists the present platform services. They will be progressively expanded:
> 1. Food and grocery delivery(includes fresh vegetables, meats, etc)
Future: 
> 2. Salon jobs
> 3. Hire Driver
> 4. AC Repair
> 5. Plumbing services
> 6. Cab services   

# ISSUES:

## Critical Issues:
> Enforce internet connection in splash activity before calling geocoder
> Enforce location is on in devices before getting lat, long

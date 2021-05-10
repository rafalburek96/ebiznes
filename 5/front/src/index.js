// eslint-disable-next-line
import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import * as serviceWorker from './serviceWorker';
import 'bootstrap/dist/css/bootstrap.min.css';
import {BrowserRouter, Route} from "react-router-dom";
import {AddressComponent} from "./components/AddressComponent";
import {BasketComponent} from "./components/BasketComponent";
import {CategoryComponent} from "./components/CategoryComponent";
import {CustomerComponent} from "./components/CustomerComponent";
import {HomeComponent} from "./components/HomeComponent";
import {OrderComponent} from "./components/OrderComponent";
import {OrderedProductsComponent} from "./components/OrderedProductsComponent";
import {PaymentComponent} from "./components/PaymentComponent";
import {ReviewComponent} from "./components/ReviewComponent";
import {StatusComponent} from "./components/StatusComponent";

 
ReactDOM.render(
    <BrowserRouter>
        <Route exact path="/address" component={AddressComponent}/>
        <Route exact path="/basket" component={BasketComponent}/>
        <Route exact path="/category" component={CategoryComponent}/>
        <Route exact path="/customer" component={CustomerComponent}/>
        <Route exact path="/" component={HomeComponent}/>
        <Route exact path="/order" component={OrderComponent}/>
        <Route exact path="/ordered-products" component={OrderedProductsComponent}/>
        <Route exact path="/payment" component={PaymentComponent}/>
        <Route exact path="/review" component={ReviewComponent}/>
        <Route exact path="/status" component={StatusComponent}/>
        
    </BrowserRouter>,
  document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();

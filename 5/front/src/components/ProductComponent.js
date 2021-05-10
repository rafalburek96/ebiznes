// eslint-disable-next-line
import React, { Component } from "react";
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import { ProductService } from "../services/ProductService";
import {OrderedProductsService} from "../services/OrderedProductsService";
 
export class ProductComponent extends Component {

    productService: ProductService;
    orderedProductService: OrderedProductsService;

    constructor() {
        super();
        this.productService = new ProductService();
        this.orderedProductService = new OrderedProductsService();
        this.state = {product: []};
    }

    async componentDidMount() {
        let res = await this.productService.getAllProducts();
        this.setState({product: res});
    }

    deleteProduct(event, id) {
        event.preventDefault();
        this.productService.deleteProduct(id)
            .then(result => console.log('Deletion succeeded ', result))
            .catch(err => console.log('An error occured during the deletion: ', err));
        window.location.reload(false);
    }

    addToBasket(event, productID, name) {
        event.preventDefault();
        this.orderedProductService.createOrderedProducts(productID, name, 1, 1)
            .then(console.log('Added to basket: '))
            .catch(err => console.log('An error occured while adding to basket: ', err));
    }

    render() {
        let styling = {
            display: 'flex',
            flexDirection: 'row',
            justifyContent: 'center',
            alignItems: 'center',
            paddingTop: '10px'
        }

        let buttonStyling = {
            position: 'absolute',
            right: '5px',
            top: '5px'
        }

        let productCard = this.state.product.map( product => (
            <Card style={{ width: '18rem', margin: '10px' }}>
                <Card.Img variant="top" src={require('../img/product.png')} />
                <Card.Body>
                    <Button variant="danger" style={buttonStyling} onClick={(event) => this.deleteProduct(event, product.id)}>X</Button>
                    <Card.Title>{ product.productName }</Card.Title>
                    <Card.Subtitle>{ product.productPrice }</Card.Subtitle>
                    <Card.Text>{ product.productDescription }</Card.Text>
                    <Button variant="success" onClick={(event) =>
                        this.addToBasket(event,
                                        product.id,
                                        product.productName)}>Add to basket</Button>
                </Card.Body>
            </Card>
        ));

        return (
            <div style={styling}>
                { productCard }
            </div>
        );
    }
}

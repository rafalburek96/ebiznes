// eslint-disable-next-line 
import React, { Component } from "react";
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import {ProductService} from "../services/ProductService";

const styling = {
    width: '800px',
    margin: '0 auto',
    paddingTop: '10px'
}

export class AddNewProductComponent extends Component {

    productService: ProductService;

    constructor() {
        super();
        this.productService = new ProductService();
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(event) {
        event.preventDefault();
        const data = new FormData(event.target);
        this.productService.createProduct(data.get('productName'),
            data.get('productDescription'),
            Number(data.get('productPrice')),
            Number(data.get('category')));
        window.location.reload(false);
    }

    render() {

        return (
            <div>
                <Form style={styling}  onSubmit={this.handleSubmit}>
                    <Form.Group controlId="formCreateProduct">
                        <Form.Label>Product name</Form.Label>
                        <Form.Control type="text"
                                      placeholder="Enter the name"
                                      name="productName"/>
                        <Form.Label>Product description</Form.Label>
                        <Form.Control as="textarea"
                                      rows="3"
                                      name="productDescription"/>
                        <Form.Label>Product price</Form.Label>
                        <Form.Control type="text"
                                      name="productPrice"/>
                        <Form.Label>Category</Form.Label>
                        <Form.Control type="text"
                                        name="category"/>
                        </Form.Group>
                    <Button variant="primary" type="submit">
                        Submit
                    </Button>
                </Form>
            </div>
        );
    }
}

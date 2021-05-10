import React, { Component } from "react";
import { ReviewService } from "../services/ReviewService";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";
import ButtonGroup from 'react-bootstrap/ButtonGroup'
import Button from 'react-bootstrap/Button'
 
export class ReviewComponent extends Component {

    reviewService: ReviewService;

    constructor() {
        super();
        this.reviewService = new ReviewService();
        this.state = {review: []};
    }

    async componentDidMount() {
        let res = await this.reviewService.getAllReviews();
        // let res = await this.orderService.getAddressByID(1)
        this.setState({review: JSON.stringify(res)});
    }

    render() {
        return (
            <div>
                <ButtonToolbar aria-label="Toolbar with button groups">
                    <ButtonGroup className="mr-2" aria-label="First group">
                        <Button>1</Button> <Button>2</Button> <Button>3</Button> <Button>4</Button> <Button>5</Button>
                    </ButtonGroup>
                    <Button variant="success">Add review</Button>
                </ButtonToolbar>
            </div>
        );
    }
}

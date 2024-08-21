import React, { useRef, useEffect, useState } from 'react';
import { addComment, fetchComments } from '../_api/comment.service';
import { addRating, fetchAvgRating } from '../_api/rating.service';
import 'bootstrap/dist/css/bootstrap.min.css';
import Comments from './miniComp/Comments';
import Rating from './miniComp/Rating';
import CommentForm from './miniComp/CommentForm';
import RatingForm from './miniComp/RatingForm';
import { Button, Form } from 'react-bootstrap';

function FeedbackService() {
    const [comments, setComments] = useState([]);
    const [avgRate, setAvgRate] = useState(0);
    const [activeTab, setActiveTab] = useState('comment&rate');  // Track the currently active tab

    const fetchData = () => {
        fetchComments("HMaM").then(response => {
            setComments(response.data);
        });
        fetchAvgRating("HMaM").then(response => {
            setAvgRate(response.data);
        });
    };

    useEffect(() => {
        fetchData();
    }, []);



    const pageStyles = {
        backgroundColor: 'rgba(193, 210, 192, 0.90)',
        width: '90%',
        display: 'flex',
        flexDirection: 'column',
        margin: 'auto',
        textAlign: 'center',
        padding: '30px 0px',
        borderRadius: '15px',
    };

    const square = {
        backgroundColor: 'rgb(214, 236, 212, 0.70)',
        width: '45%',
        borderRadius: '20px',
        padding: '40px',
        margin: 'auto',
    };

    const compons = {
        display: 'flex',
        marginTop: '30px',
    };

    const tabStyle = {
        cursor: 'pointer',
        padding: '5px 20px',
        fontSize: '16px',
        border: 'none',
        backgroundColor: 'transparent',
        outline: 'none',
        fontSize: '18px',
    };

    const activeTabStyle = {
        textDecoration: 'underline',
        fontWeight: 'bold'
    };

    const [player, setPlayer] = useState('');
    const [comment, setComment] = useState('');
    const [rating, setRating] = useState(0);



    const handleSubmit = () => {
        if (activeTab === 'comment&rate') {
            addComment("HMaM", player, comment).then(response => fetchData());
            addRating("HMaM", player, rating).then(response => fetchData());
        }
        else if (activeTab === 'comment') {
            addComment("HMaM", player, comment).then(response => fetchData());
        }
        else {
            addRating("HMaM", player, rating).then(response => fetchData());
        }

    };


    const handlePlayerChange = (newPlayer) => {
        setPlayer(newPlayer);
    };

    const handleCommentChange = (newComment) => {
        setComment(newComment);
    };

    const handleRatingChange = (newRating) => {
        setRating(newRating);
    };




    return (
        <div style={pageStyles}>
            <h2>Dear hero, give us some feedback</h2>

            <div style={compons}>
                <div style={square}>
                    <Rating rating={avgRate} />
                    <Comments comments={comments} />
                </div>
                <div style={square}>
                    <div style={{ marginBottom: '20px', display: 'flex', justifyContent: 'center' }}>
                        <button onClick={() => setActiveTab('comment&rate')} style={activeTab === 'comment&rate' ? { ...tabStyle, ...activeTabStyle } : tabStyle}>Comment & Rate</button>
                        <button onClick={() => setActiveTab('comment')} style={activeTab === 'comment' ? { ...tabStyle, ...activeTabStyle } : tabStyle}>Comment</button>
                        <button onClick={() => setActiveTab('rate')} style={activeTab === 'rate' ? { ...tabStyle, ...activeTabStyle } : tabStyle}>Rate</button>
                    </div>
                    {activeTab === 'comment&rate' && (



                        <>
                            <CommentForm onCommentChange={handleCommentChange} onPlayerChange={handlePlayerChange} />
                            <RatingForm onRatingChange={handleRatingChange} combined={true} />
                            <Button onClick={handleSubmit} >Send</Button>
                        </>


                    )}
                    {activeTab === 'comment' && (

                        <Form>
                            <h2>Add comment:</h2>
                            <CommentForm onCommentChange={handleCommentChange} onPlayerChange={handlePlayerChange} />
                            <Button onClick={handleSubmit}>Send</Button>
                        </Form>
                    )}
                    {activeTab === 'rate' && (
                        <Form>
                            <h2>Give a rating:</h2>
                            <RatingForm combined={false} onRatingChange={handleRatingChange} onPlayerChange={handlePlayerChange} />
                            <Button onClick={handleSubmit}>Send</Button>
                        </Form>
                    )}
                </div>
            </div>
        </div >
    );
}

export default FeedbackService;


{/* <Form onSubmit={handleSubmit(onSubmit)}> */ }
//  <CommentForm onSendComment={handleSendComment} />
//  <RatingForm combined={true} />
//  <Button>Send</Button>
{/* <Button type="submit" disabled={errors.comment?.message != null}>Send</Button> */ }
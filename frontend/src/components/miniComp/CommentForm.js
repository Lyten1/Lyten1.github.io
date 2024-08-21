import { Button, Form } from 'react-bootstrap';
import { useForm } from "react-hook-form";
import React from 'react';

function CommentForm({ onCommentChange, onPlayerChange }) {

    const {
        register,
        handleSubmit,
        formState: { errors },
        setValue,
        watch
    } = useForm({ mode: "onChange" });

    const comment = watch("comment");
    const player = watch("player");

    React.useEffect(() => {
        if (onCommentChange) {
            onCommentChange(comment);
        }
    }, [comment, onCommentChange]);

    React.useEffect(() => {
        if (onPlayerChange) {
            onPlayerChange(player); // Corrected from onCommentChange to onPlayerChange
        }
    }, [player, onPlayerChange]);

    return (
        <Form>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                <Form.Label>Player Name</Form.Label>
                <Form.Control
                    type="text"
                    placeholder="nickname"
                    {...register("player", { required: "Player name is required" })}
                />
            </Form.Group>
            <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                <Form.Label>Comment</Form.Label>
                <Form.Control
                    as="textarea"
                    rows={2}
                    {...register("comment", {
                        minLength: { value: 3, message: "Minimum is 3 characters." },
                        maxLength: { value: 150, message: "Maximum is 150 characters." },
                        required: { value: true, message: "Comment message is required" },
                    })}
                />
                <Form.Text style={{ color: 'red', float: 'right' }}>
                    {errors.comment?.message}
                </Form.Text>
            </Form.Group>
        </Form>
    );
}

export default CommentForm;

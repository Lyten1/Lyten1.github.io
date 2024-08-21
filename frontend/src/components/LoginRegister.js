import React, { useEffect, useState } from 'react';
import './LR.css';
import { useNavigate } from 'react-router-dom';
import CryptoJS from 'crypto-js';
import { fetchAuthorize, fetchRegister } from "../_api/user.service";



function LoginRegister() {

    const [users, setUsers] = useState({
        player1: { login: '', password: '', authorized: false, mode: 'authorize', mark: '1-st' },
        player2: { login: '', password: '', authorized: false, mode: 'authorize', mark: '2-nd' }
    });




    const hashPassword = (password) => {
        return CryptoJS.SHA256(password).toString(CryptoJS.enc.Hex);
    };




    const handleChange = (event, userNumber) => {
        const { name, value } = event.target;
        setUsers(prevUsers => ({
            ...prevUsers,
            [userNumber]: {
                ...prevUsers[userNumber],
                [name]: value
            }
        }));
    };

    const toggleForm = (userNumber) => {
        setUsers(prevUsers => ({
            ...prevUsers,
            [userNumber]: {
                ...prevUsers[userNumber],
                mode: prevUsers[userNumber].mode === 'authorize' ? 'register' : 'authorize'
            }
        }));
    };

    const registrateUser = async (userNumber) => {
        const user = users[userNumber];
        if (!user.login.trim() || !user.password.trim()) {
            alert('Please, enter login and password');
            return;
        }
        if (!/^[a-zA-Z0-9_-]+$/.test(user.login) || !/^[a-zA-Z0-9_-]+$/.test(user.password)) {
            alert('Login or password contains invalid characters. Only alphanumeric, underscore, and dash are allowed.');
            return;
        }

        const hashedPassword = hashPassword(user.password);


        try {
            const response = await fetchRegister(user.login, hashedPassword);

            if (response.data === "User registered") {
                setUsers(prevUsers => ({
                    ...prevUsers,
                    [userNumber]: { ...user, authorized: true }
                }));
                checkAllAuthorized();
            } else {
                alert('Failed to register. Please try again.');
            }
        } catch (error) {
            alert(`${error.response.data}`);
        }

        checkAllAuthorized();
    };

    const authorizeUser = async (userNumber) => {
        const user = users[userNumber];
        if (!user.login.trim() || !user.password.trim()) {
            alert('Please, enter login and password');
            return;
        }

        const otherUserNumber = userNumber === 'player1' ? 'player2' : 'player1';
        const otherUser = users[otherUserNumber];
        if (user.login === otherUser.login) {
            alert('Both players cannot have the same login. Please use a different login.');
            return;
        }

        const hashedPassword = hashPassword(user.password);

        try {
            const response = await fetchAuthorize(user.login, hashedPassword);
            if (response.data === 'User authorized') {
                setUsers(prevUsers => ({
                    ...prevUsers,
                    [userNumber]: { ...user, authorized: true }
                }));
                checkAllAuthorized();
            } else {
                alert('Failed to authorize. Please try again.');
            }
        } catch (error) {
            alert(`${error.response.data}`);
        }
        checkAllAuthorized();
    };

    const checkAllAuthorized = () => {
        if (Object.values(users).every(user => user.authorized)) {
            alert('Both users are authorized. The game begins!');
        }
    };

    const navigate = useNavigate();

    useEffect(() => {
        async function setupGame() {
            if (users.player1.authorized && users.player2.authorized) {
                navigate('/game', { replace: true, state: { users: users } });
            }
        }
        setupGame();
        localStorage.removeItem('token');
        localStorage.removeItem('gameStatus');
    }, [users]);





    return (
        <div className="containerLogin">
            <div className="game-title"><div class="undertext">Heroes of Might and Magic</div></div>
            {
                Object.entries(users).map(([userNumber, { login, password, authorized, mode, mark }]) => (
                    <div key={userNumber} className="user-form">
                        <label>
                            {mode === 'authorize' ? 'Authorize' : 'Register'}{" "}
                            <b><u>{mark}</u></b> player
                        </label>
                        <div className="login">
                            <input
                                type="text"
                                value={login}
                                onChange={(e) => handleChange(e, userNumber)}
                                name="login"
                                disabled={authorized}
                            />
                        </div>
                        <div className="password">
                            <input
                                type="password"
                                value={password}
                                onChange={(e) => handleChange(e, userNumber)}
                                name="password"
                                disabled={authorized}
                            />
                        </div>
                        <button
                            onClick={() => mode === 'authorize' ? authorizeUser(userNumber) : registrateUser(userNumber)}
                            disabled={authorized}
                            className={authorized ? "butt butt-disabled" : "butt butt-active"}
                        >
                            {mode.charAt(0).toUpperCase() + mode.slice(1)}
                        </button>
                        <div className="register">
                            <a onClick={() => toggleForm(userNumber)}>
                                {mode === 'authorize' ? 'Register' : 'Authorize'}
                            </a>
                        </div>
                    </div>
                ))
            }
        </div >
    );
}

export default LoginRegister;
import gsAxios from ".";


export const fetchRegister = async (login, password) => {
    try {
        const response = await gsAxios.post('/register', {
            login,
            password
        });
        return response;
    } catch (error) {
        console.error('Registration failed:', error);
        throw error;  // Rethrow to handle it in the calling function
    }
}

export const fetchAuthorize = async (login, password) => {
    try {
        const response = await gsAxios.post('/authorize', {
            login,
            password
        });
        return response;
    } catch (error) {
        console.error('Authorization failed:', error);
        throw error;  // Rethrow to handle it in the calling function
    }
}
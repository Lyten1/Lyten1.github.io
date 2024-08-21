import axios from 'axios';


function getBaseUrl() {
    const { protocol, hostname, port } = window.location;
    return `${protocol}//${hostname}:${port}/api`;
}


const gsAxios = axios.create({
    baseURL: getBaseUrl()
});


export default gsAxios; 
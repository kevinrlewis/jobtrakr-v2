import { initAuth0 } from '@auth0/nextjs-auth0';

export default initAuth0({
    domain: process.env.AUTH_HOST,
    clientId: process.env.CLIENT_ID,
    clientSecret: process.env.CLIENT_SECRET,
    scope: 'openid profile',
    redirectUri: 'http://localhost:3000/api/security/callback',
    postLogoutRedirectUri: 'http://localhost:3000/',
    session: {
        cookieSecret: 'some-very-very-very-very-very-very-very-very-long-secret',
        cookieLifetime: 60 * 60 * 8
    }
});

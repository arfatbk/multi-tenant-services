import type { NextAuthOptions } from "next-auth";

const issuer = process.env.OIDC_ISSUER!;

export const authOptions: NextAuthOptions = {
  providers: [
    {
      id: "oidc",
      name: "OIDC",
      type: "oauth",
      wellKnown: `${issuer}/.well-known/openid-configuration`,
      clientId: process.env.OIDC_CLIENT_ID!,
      clientSecret: process.env.OIDC_CLIENT_SECRET!,
      authorization: {
        url: `${issuer}/oauth2/authorize`,
        params: { scope: "openid" },
      },
      token: `${issuer}/oauth2/token`,
      userinfo: `${issuer}/userinfo`,
      checks: [],
      profile(profile) {
        return {
          id: profile.sub,
          name: profile.name || profile.preferred_username || profile.sub,
          email: profile.email,
        };
      },
    },
  ],
  session: {
    strategy: "jwt",
  },
  callbacks: {
    async jwt({ token, account }) {
      if (account) {
        token.accessToken = account.access_token;
        token.idToken = account.id_token;
      }
      return token;
    },
    async session({ session, token }) {
      session.accessToken = token.accessToken as string;
      session.idToken = token.idToken as string;
      return session;
    },
  },
};

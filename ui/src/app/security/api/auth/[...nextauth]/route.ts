import NextAuth from "next-auth";
import { authOptions } from "../authOptions";

const handler = NextAuth(authOptions);
export { handler as GET, handler as POST };

// Extend Session type for accessToken/idToken
import "next-auth";
declare module "next-auth" {
  interface Session {
    accessToken?: string;
    idToken?: string;
  }
}

"use client";
import { SessionProvider } from "next-auth/react";

export default function SessionProviderWrapper({ children }: { children: React.ReactNode }) {
  return <SessionProvider basePath='/security/api/auth'>{children}</SessionProvider>;
}

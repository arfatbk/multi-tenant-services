"use client";
import { signIn } from "next-auth/react";
import { useSearchParams } from "next/navigation";
import Image from "next/image";
import Navbar from "@/components/Navbar";

export default function LoginPage() {
  const searchParams = useSearchParams();
  const callbackUrl = searchParams.get("callbackUrl") || "/";

  return (
    <>
      <Navbar />
      <main
        style={{
          minHeight: "100vh",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          background: "linear-gradient(135deg, #e0e7ef 0%, #fff 100%)",
          padding: 40,
        }}
      >
        <div
          style={{
            background: "#fff",
            borderRadius: 16,
            boxShadow: "0 4px 24px #0002",
            padding: "48px 32px",
            maxWidth: 400,
            width: "100%",
            textAlign: "center",
          }}
        >
          <Image
            src="/next.svg"
            alt="Logo"
            width={60}
            height={32}
            style={{ marginBottom: 24 }}
          />
          <h1
            style={{
              fontSize: 28,
              marginBottom: 12,
              color: "#222",
            }}
          >
            Sign in to Fable UI
          </h1>
          <p
            style={{
              color: "#666",
              marginBottom: 32,
            }}
          >
            Secure authentication with OpenID Connect
          </p>
          <button
            onClick={() =>
              signIn("oidc", { callbackUrl, basePath: "/security/auth" })
            }
            style={{
              padding: "12px 32px",
              fontSize: 18,
              background: "#0070f3",
              color: "#fff",
              border: "none",
              borderRadius: 8,
              fontWeight: 600,
              cursor: "pointer",
              marginBottom: 12,
            }}
          >
            Sign in with OpenID Connect
          </button>
        </div>
      </main>
    </>
  );
}

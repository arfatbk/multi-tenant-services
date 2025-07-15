"use client";
import Link from "next/link";
import { useSession } from "next-auth/react";

export default function Navbar() {
  const { data: session } = useSession();

  return (
    <nav style={{ display: "flex", justifyContent: "space-between", alignItems: "center", padding: "1.5rem 2rem", background: "#222", borderBottom: "1px solid #eee" }}>
      <div style={{ fontWeight: 700, fontSize: 24 }}>
        <Link href="/" style={{ textDecoration: "none", color: "#fff" }}>Fable UI</Link>
      </div>
      <div style={{ display: "flex", gap: 24, alignItems: "center" }}>
        <Link href="/dashboard" style={{ color: "#fff", textDecoration: "none" }}>Dashboard</Link>
        {session ? (
          <Link href="/logout" style={{ color: "#fff", textDecoration: "none" }}>Sign out</Link>
        ) : (
          <Link href="/login" style={{ color: "#fff", textDecoration: "none" }}>Sign in</Link>
        )}
      </div>
    </nav>
  );
}

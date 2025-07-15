import Link from "next/link";
import { getServerSession } from "next-auth";
import { redirect } from "next/navigation";
import { authOptions } from "@/app/security/api/auth/authOptions";
import Image from "next/image";

export default async function Home() {
  const session = await getServerSession(authOptions);
  if (session) {
    redirect("/dashboard");
  }

  return (
    <div style={{ minHeight: "100vh", display: "flex", flexDirection: "column" }}>
      {/* Navbar */}
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

      {/* Hero Section */}
      <section style={{ display: "flex", flex: 1, alignItems: "center", justifyContent: "center", padding: "3rem 2rem", background: "#181c20" }}>
        <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 40, maxWidth: 1100, width: "100%" }}>
          <div style={{ display: "flex", flexDirection: "column", justifyContent: "center" }}>
            <h1 style={{ fontSize: 40, marginBottom: 20, color: "#fff" }}>Welcome to Fable UI Home</h1>
            <p style={{ fontSize: 20, color: "#d1d5db", marginBottom: 24 }}>
              Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque euismod, nisi eu consectetur consectetur, nisl nisi consectetur nisi, euismod euismod nisi nisi euismod.
            </p>
            <ul style={{ fontSize: 16, color: "#b0b8c1", marginBottom: 24 }}>
              <li>Modern authentication with OpenID Connect</li>
              <li>Protected routes and custom login</li>
              <li>Beautiful, responsive UI</li>
            </ul>
            <Link href="/dashboard" style={{ background: "#0070f3", color: "#fff", padding: "12px 32px", borderRadius: 8, fontWeight: 600, textDecoration: "none", fontSize: 18, width: "fit-content" }}>
              Go to Dashboard Page
            </Link>
          </div>
          <div style={{ display: "flex", alignItems: "center", justifyContent: "center" }}>
            <div style={{
              background: "linear-gradient(135deg, #fff 60%, #e0e7ef 100%)",
              borderRadius: 16,
              boxShadow: "0 4px 24px #0001",
              padding: 24,
              display: "flex",
              alignItems: "center",
              justifyContent: "center"
            }}>
              <Image src="/next.svg" alt="Sample" width={320} height={180} style={{ filter: "brightness(1.2)" }} />
            </div>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer style={{ textAlign: "center", padding: "1.5rem 0", background: "#f5f5f5", borderTop: "1px solid #eee", fontSize: 16, color: "#888" }}>
        &copy; {new Date().getFullYear()} Fable UI. All rights reserved.
      </footer>
    </div>
  );
}

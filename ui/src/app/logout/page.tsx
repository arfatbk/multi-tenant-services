"use client";
import { useEffect } from "react";
import { signOut } from "next-auth/react";
import { useRouter } from "next/navigation";
import Navbar from "@/components/Navbar";

export default function LogoutPage() {
  const router = useRouter();
  useEffect(() => {
    signOut({ callbackUrl: "/" });
  }, [router]);

  return (
    <>
      <Navbar />
      <main style={{ padding: 40, textAlign: "center" }}>
        <h1>Signing you out...</h1>
      </main>
    </>
  );
}

import { getServerSession } from "next-auth";
import { redirect } from "next/navigation";
import { authOptions } from "@/app/security/api/auth/authOptions";
import Navbar from "@/components/Navbar";

export default async function ProtectedPage() {
  const session = await getServerSession(authOptions);
  if (!session) {
    redirect("/login?callbackUrl=/dashboard");
  }
  return (
    <>
      <Navbar />
      <main style={{ padding: 40 }}>
        <h1>Dashboard Page</h1>
        <p>Welcome, {session.user?.email || session.user?.name}!</p>
        <pre>{JSON.stringify(session, null, 2)}</pre>
      </main>
    </>
  );
}

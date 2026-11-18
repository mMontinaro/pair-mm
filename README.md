# 🧩 Backend Engineering Exercise — Project *Pair*

Welcome. This exercise evaluates how you turn **real-world, messy requirements** into a clean,
secure **Spring Boot** backend. It is deliberately closer to a day on the job than to a textbook
assignment: nobody hands you a tidy checklist of tasks.

---

## 🎬 The scenario

You have just joined the Sara engineering team and inherited **Project *Pair***: the backend of a
**password manager** (a *vault* that stores users' credentials, personal information and payment cards).

The product and technical requirements were **not** written as a single specification. They arrived
over a couple of weeks the way they usually do in real life — as **emails, threads and attached
documents** between the Product Owner, a Solution Architect, the Project Manager, a Business Analyst
and a frontend developer.

All of that material is in the **[`docs/`](./docs)** folder.

> ⚠️ **It is your job to reconstruct the specification.**
> Read the material, **put it in chronological order**, reconcile decisions that changed over time,
> and figure out what the API must actually do. Not everything in `docs/` is relevant: some messages
> are noise, and some refer to a *different* project. Part of the exercise is telling signal from noise.

When requirements conflict, the **most recent decision wins** — just like in a real inbox.

---

## 🎯 What we evaluate

We do **not** grade you on reproducing a hidden "expected" implementation. We look at the engineering:

- 🔐 **Security & cryptography** — how sensitive data is protected, in transit decisions, and **at rest**.
- 🧱 **Architecture & code quality** — clean layering, separation of concerns, readable code.
- ✅ **Correctness** — does the API behave exactly as the (reconstructed) specification requires?
- 🧪 **Testing & documentation** — meaningful tests and clear documentation.
- 🧠 **Judgement** — did you follow the *actual, current* requirements, or the first/easiest reading?

---

## ⚙️ Technical ground rules

- The project is a **Spring Boot** application backed by an **H2** in-memory database.
  The runtime is already pinned in [`pom.xml`](./pom.xml) — **you build everything else from scratch**
  (there is intentionally no application class yet).
- It must **run locally with zero external setup**: a reviewer will start it and call the API.
- On startup, the application must **seed at least 30 records** into the database, covering the
  different item types, so the search and detail endpoints can be exercised immediately.
- Keep the existing **layered architecture** conventions used across Sara projects (web → service →
  database, request/response envelopes, a shared status object). The details you need are in `docs/`.

---

## 📬 Submission

Complete the work on a **fork** of this repository, commit and push everything, then send the
repository link by email to: **f.mori@sara.it**

---

## 🤖 Bonus — AI-assisted development (optional)

Using an AI assistant (Claude Code, Gemini CLI, Copilot, Cursor, …) is **allowed but not required**.

If you do use one, add an **`/ai-assisted/`** folder documenting the *process*: the context you gave
the tool, the key prompts, where it got things **wrong**, and how you corrected and governed its output.
We value honesty here — a report that describes the tool's failures and how you caught them is worth
more than one that only shows successes. The goal is to see how you **drive** the tool, not to let it
drive you.

---

## ❓ Follow-up question (include a short answer in your submission email)

> The design you will implement encrypts secrets **server-side** using a master key held by the server.
> How would you evolve it toward a **zero-knowledge** model, where the server never sees secrets in
> clear text? What are the trade-offs for search, password reset and data recovery?

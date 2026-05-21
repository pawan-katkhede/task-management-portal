# AI-Powered Task Management Portal - Submission Document

## Assumptions

1. Internet connection required for AI feature (Gemini API)
2. MySQL must be installed locally on user's machine
3. Gemini API key is free from Google AI Studio
4. Java 21 and Node.js 18+ required
5. Tested on Chrome browser

## AI Workflow

1. User enters task title
2. User clicks "AI" button
3. Frontend calls /api/ai/generate
4. Backend calls Google Gemini API
5. Gemini returns description, priority, estimated hours
6. Frontend auto-fills the form

**Fallback:** If API fails, default response is used.

## Blockchain Implementation

Not attempted - focused on mandatory requirements.

## Submission

- Name: Pawan Katkhede
- Date: 21 May 2026
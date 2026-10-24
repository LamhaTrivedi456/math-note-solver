const form = document.getElementById("solve-form");
const input = document.getElementById("expression");
const errorBox = document.getElementById("error");
const resultBox = document.getElementById("result");
const resultType = document.getElementById("result-type");
const stepsList = document.getElementById("steps");
const answerBox = document.getElementById("answer");

function showError(message) {
  resultBox.hidden = true;
  errorBox.textContent = message;
  errorBox.hidden = false;
}

function showResult(data) {
  errorBox.hidden = true;
  resultType.textContent = data.type === "equation" ? "Equation steps" : "Calculation steps";
  stepsList.replaceChildren();

  if (data.steps.length === 0) {
    const li = document.createElement("li");
    li.textContent = "No calculation needed";
    stepsList.appendChild(li);
  } else {
    data.steps.forEach((step) => {
      const li = document.createElement("li");
      li.textContent = step;
      stepsList.appendChild(li);
    });
  }

  answerBox.textContent = data.answer;
  resultBox.hidden = false;
}

async function solve(text) {
  try {
    const response = await fetch("/api/solve", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ input: text }),
    });
    const data = await response.json();
    if (!response.ok || !data.success) {
      showError(data.error || "Something went wrong");
      return;
    }
    showResult(data);
  } catch (e) {
    showError("Could not reach the server. Is the app running?");
  }
}

form.addEventListener("submit", (event) => {
  event.preventDefault();
  solve(input.value);
});

document.querySelectorAll(".chip").forEach((chip) => {
  chip.addEventListener("click", () => {
    input.value = chip.dataset.example;
    solve(input.value);
  });
});
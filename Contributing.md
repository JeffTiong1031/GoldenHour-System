# 🤝 Contributing

## Getting Started
```bash
# Clone
git clone https://github.com/zerngit/GoldenHour-System.git
cd GoldenHour-System

# Build
cd goldenhour
mvn clean install

# Run (app)
mvn exec:java -Dexec.mainClass="com.goldenhour.main.Main"

# One-time migration (optional)
mvn exec:java -Dexec.mainClass="com.goldenhour.main.SyncDataCSVSQL"
```

## Branches
```bash
git fetch origin
git checkout feature/YOUR_FEATURE # edit YOUR_FEATURE to the feature you work on
```

## Daily Workflow
```bash
git checkout main
git pull origin main
git checkout feature/your-branch
git merge main

# Work, then:
git add .
git commit -m "Describe changes"
git push -u origin feature/your-branch
```

## Submit a PR
- Base: `main` ← Compare: `feature/your-branch`
- Title: `[Feature] <name> Complete`
- Request review from `zerngit`

---

**Last Updated:** December 23, 2025 | **Version:** 2.0.0-Beta
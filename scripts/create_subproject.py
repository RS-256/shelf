#!/usr/bin/env python3

import json
import os
import shutil

json_file = "versions/versions.json"

version = input("Enter the new version (e.g., 26.1.2): ").strip()

if not version:
    print("error: Version cannot be empty.")
    exit(1)

if os.path.exists(json_file):
    with open(json_file, "r") as f:
        try:
            versions = json.load(f)
        except json.JSONDecodeError:
            versions = []
else:
    versions = []

if version not in versions:
    versions.append(version)

with open(json_file, "w") as f:
    json.dump(versions, f, indent=2)

target_dir = os.path.join("versions", version)
source_dir = os.path.join("versions", "future")

os.makedirs(target_dir, exist_ok=True)

if os.path.exists(source_dir):
    for filename in os.listdir(source_dir):
        if filename == "gradle.properties" or filename.endswith(".accesswidener"):
            source_file = os.path.join(source_dir, filename)
            target_file = os.path.join(target_dir, filename)
            shutil.copy2(source_file, target_file)
else:
    print(f"Warning: Source directory '{source_dir}' does not exist. No files copied.")

version_parts = version.split(".")
if len(version_parts) >= 3:
    try:
        major = int(version_parts[0])
        minor = int(version_parts[1])
        patch = int(version_parts[2])
        formatted_num0 = f"{major:02d}{minor:02d}{patch:02d}"
        formatted_num1 = f"{major}_{minor:02d}_{patch:02d}"
    except ValueError:
        formatted_num0 = version.replace(".", "_")
        formatted_num1 = formatted_num0
else:
    formatted_num0 = version.replace(".", "_")
    formatted_num1 = formatted_num0

print("\n--- Copy and paste the following to build.gradle ---")
print(f"def mc{formatted_num0} = createNode('{version}', {formatted_num1}, '')")
print("-----------------------------------------")

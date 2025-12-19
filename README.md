# CircleFight (Circle Fighter Simulation)

A small **2D circle-fighting simulation** written in **Java**, built to generate short, satisfying clips (e.g. for **Instagram Reels**).

Two circles (Red & Blue) spawn into an arena, take damage, pick up items, and “fight” via simple simulation rules — ideal for recording quick sequences.

---

## Demo / Screenshots

### Saw pickup → equipped → hit sequence
| Item spawned | Equipped | Close-up / in action | Hit (damage pop-up) |
|---|---|---|---|
| ![Saw item](screenshots/sawitem.png) | ![Equipped saw](screenshots/equippedsaw.png) | ![Equipped saw (alt)](screenshots/equippedsaw1.png) | ![Hit](screenshots/hit.png) |

### Heart pickup → heal
| Item spawned | Heal |
|---|---|
| ![Heart item](screenshots/heartitem.png) | ![Heal](screenshots/heal.png) |

---

## What it does

- **2D arena simulation** with boundary walls
- **Two fighters** (Blue vs Red) with **health bars**
- **Damage/heal feedback** (e.g., floating numbers)
- **Item system** (currently showcased in README):
  - **Saw**: offensive item that enables damage
  - **Heart**: restores health

---

## Quick start

### Prerequisites
- **Java JDK** installed (a recent JDK is recommended)
- A machine capable of running **OpenGL** (this is a windowed simulation)

### Run
This repo includes a convenience script:

```bash


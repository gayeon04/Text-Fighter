package com.hotmail.kalebmarc.textfighter.main;

import com.hotmail.kalebmarc.textfighter.battle.AttackStrategies;
import com.hotmail.kalebmarc.textfighter.battle.AttackStrategy;
import com.hotmail.kalebmarc.textfighter.battle.BattleAnalyzer;
import com.hotmail.kalebmarc.textfighter.battle.BattleManager;
import com.hotmail.kalebmarc.textfighter.battle.BattleRecord;
import com.hotmail.kalebmarc.textfighter.enemy.EnemyRegistry;
import com.hotmail.kalebmarc.textfighter.item.*;
import com.hotmail.kalebmarc.textfighter.player.*;
import com.hotmail.kalebmarc.textfighter.quest.CriticalQuest;
import com.hotmail.kalebmarc.textfighter.quest.GameEvent;
import com.hotmail.kalebmarc.textfighter.quest.KillQuest;
import com.hotmail.kalebmarc.textfighter.quest.Quest;
import com.hotmail.kalebmarc.textfighter.quest.QuestManager;
import com.hotmail.kalebmarc.textfighter.util.AutoSaveTask;
import com.hotmail.kalebmarc.textfighter.util.GameLogger;

import time.GameClock;

import javax.swing.*;
import java.util.Scanner;

import static com.hotmail.kalebmarc.textfighter.player.Health.getStr;
import static com.hotmail.kalebmarc.textfighter.player.Health.upgrade;
import static com.hotmail.kalebmarc.textfighter.player.Settings.menu;
import static com.hotmail.kalebmarc.textfighter.player.Settings.setDif;
import static java.util.Arrays.asList;

/**
 * [개선 후] Game.java - 모든 시스템 연동
 *
 * 1. EnemyRegistry (Factory Pattern)
 *    기존: public static Enemy zombie; goblin; ... 하드코딩 10개 선언
 *    개선: EnemyRegistry.initDefault() 한 줄로 모든 적 등록
 *
 * 2. BattleManager (Strategy Pattern)
 *    기존: if (weapon.equals("Sniper")) { if (fightPath<=30) ... } else { ... }
 *    개선: updateBattleStrategy()로 무기별 전략 자동 교체
 *
 * 3. BattleRecord + BattleAnalyzer (Stream/Optional)
 *    기존: 전투 통계 없음
 *    개선: 게임 종료 시 칭호/총평 자동 분석 출력
 *
 * 4. QuestManager (Observer Pattern)
 *    기존: 퀘스트 시스템 없음
 *    개선: notify() 한 줄로 퀘스트 자동 처리
 *
 * 5. AutoSaveTask (멀티스레드)
 *    기존: 수동 저장만 존재 (case 10)
 *    개선: 별도 스레드에서 30초마다 자동저장
 *
 * 6. GameLogger (Singleton)
 *    기존: 로그 시스템 없음
 *    개선: 전투/저장/에러 로그 통합 관리
 */
public class Game {

	private final static Scanner SCAN = new Scanner(System.in);
	private static boolean gameStarted = false;

	// 기존 Enemy static 변수 유지 (Settings.java 호환성)
	// EnemyRegistry.initDefault()와 병행 사용
	public static Enemy darkElf;
	public static Enemy ninja;
	public static Enemy giantSpider;
	public static Enemy zombie;
	public static Enemy goblin;
	public static Enemy ghost;
	public static Enemy barbarian;
	public static Enemy giantAnt;
	public static Enemy evilUnicorn;
	public static Enemy ogre;

	public static boolean hadGameStarted() { return gameStarted; }

	// ── 기존 Enemy static 변수 제거 → EnemyRegistry로 대체 ──────────
	// 기존: public static Enemy zombie; goblin; ghost; ninja; ...
	// 개선: EnemyRegistry.initDefault() 로 한 번에 등록 (Factory Pattern)

	// Weapons (기존 유지)
	public static Weapon fists;
	public static Weapon baseballBat;
	public static Weapon knife;
	public static Weapon pipe;
	public static Weapon pistol;
	public static Weapon smg;
	public static Weapon shotgun;
	public static Weapon rifle;
	public static Weapon sniper;
	public static Weapon chainsaw;

	// Armours (기존 유지)
	public static Armour none     = new Armour("None",     0,   0,  1);
	public static Armour basic    = new Armour("Basic",    400, 15, 5);
	public static Armour advanced = new Armour("Advanced", 750, 30, 7);

	// Food (기존 유지)
	public static Food apple       = new Food("Apple",         "A boring 'ol apple.",                StatusEffect.type.HEALTH, Food.type.FRUIT,      5);
	public static Food orange      = new Food("Orange",        "Sort of like an apple, but orange.", StatusEffect.type.HEALTH, Food.type.FRUIT,      5);
	public static Food dragonfruit = new Food("Dragon Fruit",  "Unfortunately, not a real dragon.",  StatusEffect.type.HEALTH, Food.type.FRUIT,      10);
	public static Food meat        = new Food("Chunk of meat", "Probably not rotten.",               StatusEffect.type.HEALTH, Food.type.MEAT_OTHER, 15);
	public static Food mushroom    = new Food("Mushroom",      "The good kind!",                     StatusEffect.type.HEALTH, Food.type.OTHER,      5);
	public static Food fish        = new Food("Fish",          "Found in rivers and lakes.",         StatusEffect.type.HEALTH, Food.type.MEAT_FISH,  15);

	// 신규 시스템
	private static BattleManager battleManager;
	private static BattleRecord  battleRecord;
	private static AutoSaveTask  autoSave;
	private static final GameLogger    logger       = GameLogger.getInstance();
	private static final QuestManager  questManager = QuestManager.getInstance();

	public void start() {

		// Factory Pattern: 적 등록, settings.java에서 적 등록
		//EnemyRegistry.initDefault();

		// Strategy Pattern: 기본 전략(근접)으로 시작
		battleManager = new BattleManager(AttackStrategies.MELEE);
		battleRecord  = new BattleRecord(User.name());

		// 멀티스레드: 30초마다 자동저장
		autoSave = new AutoSaveTask(() -> {
			Saves.save(false);
			logger.save("Auto-save complete");
		}, 30);

		// Observer Pattern: quest registration
		questManager.subscribe(new KillQuest("Zombie Hunter",  "Zombie", 3,  50));
		questManager.subscribe(new KillQuest("Monster Hunter", null,     5, 100));
		questManager.subscribe(new CriticalQuest("Critical Master", 2, 80));

		logger.info("Game started");

		GameUtils.showPopup(Constants.HEADER,
				Constants.SUB_HEADER,
				asList("Do you want to load your game", "from save file?"),
				asList("Exit to Main", "Yes", "No")
		);

		int choice = Ui.getValidInt();

		switch (choice) {
			case 1:
				autoSave.stop();
				return;
			case 2:
				if (Saves.load()) {
					gameStarted = true;
					GameClock.startTimeClock();
					autoSave.start();
					break;
				} else {
					autoSave.stop();
					return;
				}
			default:
				String difficultyLevel = getDifficulty();
				if (difficultyLevel.equals("Exit")) {
					autoSave.stop();
					return;
				} else {
					setDif(difficultyLevel, true, false);
					Health.set(100, 100);
					User.promptNameSelection();
					Enemy.encounterNew();
					Saves.save(true);
					gameStarted = true;
					GameClock.startTimeClock();
					autoSave.start();
					battleRecord = new BattleRecord(User.name());
					break;
				}
		}

		while (true) {

			if (Stats.kills > Stats.highScore) Stats.highScore = Stats.kills;
			Achievements.check();

			// Strategy Pattern: 무기 변경 시 전략 자동 교체
			updateBattleStrategy();

			Ui.cls();

			final String DIV  = "==================================================================";
			final String LINE = "------------------------------------------------------------------";

			GameClock.updateGameTime();

			Ui.println(DIV);
			String title = "  Text-Fighter " + Version.getFull();
			String scoreStr = "Kill: " + Stats.kills + " | Best: " + Stats.highScore + "  ";
			int pad = DIV.length() - title.length() - scoreStr.length();
			Ui.println(title + " ".repeat(Math.max(1, pad)) + scoreStr);
			if (Cheats.enabled()) Ui.println("  !! CHEATS ACTIVATED !!");
			String godMsg = Settings.godModeMsg();
			if (godMsg != null && !godMsg.isEmpty()) Ui.println("  " + godMsg);
			Ui.println(LINE);
			Ui.println("  Player : " + User.name() + "   Lv." + Xp.getLevel() + "  " + Xp.getFull());
			Ui.println("  Health : " + getStr() + "   Coins: " + Coins.get() + "   Armour: " + Armour.getEquipped().toString());
			Ui.println("  Weapon : " + Weapon.get().getName() + "   FA: x" + FirstAid.get() + "   Surv: x" + Potion.get("survival") + "   Rec: x" + Potion.get("recovery"));
			Weapon.displayAmmo();
			Ui.println("  Time   : " + GameClock.getGameDate() + "   " + GameClock.getGameTime());
			Ui.println(LINE);
			Ui.println("  Enemy  : " + Enemy.get().getName() + "   Health: " + Enemy.get().getHeathStr() + "   First-Aid: " + Enemy.get().getFirstAidKit());
			Ui.println(DIV);
			Ui.println("  IMPLEMENTED SYSTEMS");
			Ui.println(String.format("  [Strategy Pattern]  %-14s  [Singleton Logger]  %d logs",
					battleManager.getStrategyName(), logger.size()));
			Ui.println(String.format("  [Observer/Quest]    %d active, %d done  [Thread AutoSave]   %s",
					questManager.getActiveQuests().size(),
					questManager.getCompletedQuests().size(),
					autoSave.isRunning() ? "Running (x" + autoSave.getSaveCount() + ")" : "Stopped"));
			if (!questManager.getActiveQuests().isEmpty()) {
				StringBuilder qb = new StringBuilder("    ");
				questManager.getActiveQuests().stream()
						.filter(o -> o instanceof Quest)
						.map(o -> (Quest) o)
						.forEach(q -> qb.append(q.getTitle()).append(" ").append(q.getProgressStr()).append("  "));
				Ui.println(qb.toString());
			}
			Ui.println(DIV);
			Ui.println("  1) Fight        2) Home         3) Town");
			Ui.println("  4) First-Aid    5) Potion       6) Eat Food");
			Ui.println("  7) Insta-Heal   8) Use Power    9) Run Away");
			Ui.println("  10) Quit");
			Ui.println(DIV);

			switch (Ui.getValidInt()) {

				case 1:
					// [핵심 변경] Strategy Pattern 적용
					// 기존: if (weapon.equals("Sniper")) { if (fightPath<=30) ... } else { ... }
					// 개선: battleManager가 무기에 맞는 전략으로 데미지 계산
					int damage = battleManager.attack(
							Weapon.get().getDamageMin(),
							Weapon.get().getDamageMax(),
							Weapon.get().getName()
					);

					if (damage > 0) {
						boolean killed = Enemy.get().takeDamage(damage);
						logger.battle(Weapon.get().getName() + " -> " + damage + " damage");

						// record only ATTACK or CRITICAL per hit to avoid double-counting
						boolean isCritical = damage >= Weapon.get().getDamageMax() * 1.8;
						if (isCritical) {
							battleRecord.record(BattleRecord.EventType.CRITICAL, damage, Weapon.get().getName());
							questManager.notify(GameEvent.CRITICAL_HIT, damage);
						} else {
							battleRecord.record(BattleRecord.EventType.ATTACK, damage, Weapon.get().getName());
						}

						Ui.println("----------------------------------------------------");
						Ui.println("You attacked " + Enemy.get().getName() + "!");
						Ui.println(Weapon.get().getName() + " dealt " + damage + " damage!");
						Ui.println("----------------------------------------------------");
						Ui.println("Your health: " + getStr());
						Ui.println("Enemy health: " + Enemy.get().getHeathStr());
						Ui.println("----------------------------------------------------");

						if (killed) {
							questManager.notify(GameEvent.ENEMY_KILLED, Enemy.get().getName());
							battleRecord.record(BattleRecord.EventType.KILL, 1, Enemy.get().getName());
							logger.battle(Enemy.get().getName() + " defeated!");
						}

					} else {
						battleRecord.record(BattleRecord.EventType.MISS, 0, Weapon.get().getName());
						int enemyDmg = Enemy.get().getDamageMin()
								+ new java.util.Random().nextInt(
								Math.max(1, Enemy.get().getDamageMax() - Enemy.get().getDamageMin())
						);
						Health.takeDamage(enemyDmg);
						battleRecord.record(BattleRecord.EventType.HIT, enemyDmg, Enemy.get().getName());
						logger.battle("Missed! " + Enemy.get().getName() + " counterattacked -> " + enemyDmg + " damage");
						Ui.println("----------------------------------------------------");
						Ui.println("Attack missed!");
						Ui.println(Enemy.get().getName() + " counterattacked -> " + enemyDmg + " damage!");
						Ui.println("----------------------------------------------------");
						Ui.println("Your health: " + getStr());
						Ui.println("Enemy health: " + Enemy.get().getHeathStr());
						Ui.println("----------------------------------------------------");
					}
					Ui.pause();
					break;

				case 2:
					home();
					break;

				case 3:
					town();
					break;

				case 4:
					FirstAid.use();
					battleRecord.record(BattleRecord.EventType.POTION_USED, 20, "First-Aid Kit");
					break;

				case 5:
					Ui.cls();
					Ui.println("Which potion would you like to use?");
					Ui.println("1) Survival Potion");
					Ui.println("2) Recovery Potion");
					Ui.println("3) Back");
					switch (Ui.getValidInt()) {
						case 1:
							Potion.use("survival");
							battleRecord.record(BattleRecord.EventType.POTION_USED, 0, "Survival Potion");
							break;
						case 2:
							Potion.use("recovery");
							battleRecord.record(BattleRecord.EventType.POTION_USED, 0, "Recovery Potion");
							break;
					}
					break;

				case 6:
					Food.list();
					break;

				case 7:
					InstaHealth.use();
					break;

				case 8:
					Power.use();
					break;

				case 9:
					questManager.notify(GameEvent.RAN_AWAY, null);
					battleRecord.record(BattleRecord.EventType.RAN_AWAY, 0, "Ran away");
					logger.info("Ran from battle");
					Ui.cls();
					Ui.popup("You ran away from the battle.", "Ran Away", JOptionPane.INFORMATION_MESSAGE);
					Enemy.encounterNew();
					break;

				case 10:
					Stats.timesQuit++;
					autoSave.stop();
					printBattleReport();
					questManager.printStatus();
					logger.info("Game ended");
					Ui.pause();
					return;

				case 0:
					Cheats.cheatGateway();
					break;

				case 99:
					Debug.menu();
					break;

				default:
					break;

			} // switch
		} // while
	}

	/**
	 * 무기에 따라 BattleManager 전략 교체 (Strategy Pattern 핵심)
	 *
	 * 기존: case 1 안에 if/else 하드코딩
	 * 개선: 무기 이름 → 전략 객체 동적 교체
	 */
	private void updateBattleStrategy() {
		AttackStrategy strategy;
		switch (Weapon.get().getName()) {
			case "Sniper":   strategy = AttackStrategies.SNIPER;   break;
			case "Shotgun":  strategy = AttackStrategies.SHOTGUN;  break;
			default:         strategy = AttackStrategies.MELEE;    break;
		}
		battleManager.setStrategy(strategy);
	}

	/**
	 * 게임 종료 시 BattleAnalyzer로 전투 분석 리포트 출력 (Stream/Optional 활용)
	 */
	private void printBattleReport() {
		if (battleRecord.getTotalTurns() > 0) {
			BattleAnalyzer analyzer = new BattleAnalyzer();
			analyzer.printReport(analyzer.analyze(battleRecord));
		}
	}

	private static void town() {
		while (true) {
			Ui.cls();
			Ui.println("------------------------------------------------------------------");
			Ui.println("                           Welcome to Town                          ");
			Ui.println("--Score Info--");
			Ui.println("     Kill Streak: " + Stats.kills);
			Ui.println("     Highest Kill Streak: " + Stats.highScore);
			Ui.println("--Player Info--");
			Ui.println("     Health: " + getStr());
			Ui.println("     Coins: " + Coins.get());
			Ui.println("     First-Aid kits: " + FirstAid.get());
			Ui.println("     Potions: ");
			Ui.println("          Survival: " + Potion.get("survival"));
			Ui.println("          Recovery: " + Potion.get("recovery"));
			Ui.println("     Equipped Weapon: " + Weapon.get().getName());
			Ui.println("------------------------------------------------------------------");
			Ui.println("1) Casino");
			Ui.println("2) Home");
			Ui.println("3) Bank");
			Ui.println("4) Shop");
			Ui.println("5) Upgrade health");
			Ui.println("6) Back");
			Ui.println("------------------------------------------------------------------");

			switch (Ui.getValidInt()) {
				case 1: Casino.menu(); break;
				case 2: home();        break;
				case 3: Bank.menu();   break;
				case 4: Shop.menu();   break;
				case 5: upgrade();     break;
				case 6: return;
				default: break;
			}
		}
	}

	private static void home() {
		while (true) {
			Ui.cls();
			Ui.println("------------------------------------------------------------------");
			Ui.println("                             Welcome Home                            ");
			Ui.println("--Score Info--");
			Ui.println("     Kill Streak: " + Stats.kills);
			Ui.println("     Highest Kill Streak: " + Stats.highScore);
			Ui.println("--Player Info--");
			Ui.println("     Health: " + getStr());
			Ui.println("     Coins: " + Coins.get());
			Ui.println("     First-Aid kits: " + FirstAid.get());
			Ui.println("     Potions: ");
			Ui.println("          Survival: " + Potion.get("survival"));
			Ui.println("          Recovery: " + Potion.get("recovery"));
			Ui.println("     Equipped Weapon: " + Weapon.get().getName());
			Ui.println("------------------------------------------------------------------");
			Ui.println("1) Equip weapon");
			Ui.println("2) Equip armour");
			Ui.println("3) View chest");
			Ui.println("4) Achievements");
			Ui.println("5) Stats");
			Ui.println("6) About");
			Ui.println("7) Settings");
			Ui.println("8) Help");
			Ui.println("9) Credits");
			Ui.println("10) Back");
			Ui.println("------------------------------------------------------------------");

			switch (Ui.getValidInt()) {
				case 1:  Weapon.choose();                                   break;
				case 2:  Armour.choose();                                   break;
				case 3:  Chest.view();                                      break;
				case 4:  Achievements.view();                               break;
				case 5:  Stats.view();                                      break;
				case 6:  About.view(true); Achievements.viewedAbout = true; break;
				case 7:  menu();                                            break;
				case 8:  Help.view();                                       break;
				case 9:  Credits.view();                                    break;
				case 10: return;
				default: break;
			}
		}
	}

	private static String getDifficulty() {
		GameUtils.showPopup(Constants.HEADER,
				Constants.SUB_HEADER,
				asList("Choose a difficulty"),
				asList("Exit", "Easy", "Hard")
		);
		if (!SCAN.hasNextInt()) { Ui.cls(); return "Exit"; }
		int c = SCAN.nextInt();
		if (c == 2) { Ui.cls(); return "Easy"; }
		else if (c == 3) { Ui.cls(); return "Hard"; }
		else { Ui.cls(); return "Exit"; }
	}
}
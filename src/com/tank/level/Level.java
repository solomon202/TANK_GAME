package com.tank.level;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.tank.game.Game;
import com.tank.game.Bonus;
import com.tank.game.Entity;
import com.tank.game.level.InfoPanel;
import com.tank.game.level.Tile;
import com.tank.game.level.TileType;
import com.tank.graphics.Sprite;
import com.tank.graphics.SpriteSheet;
import com.tank.graphics.TextureAtlas;
import com.tank.utils.Utils;
//уравень 
public class Level {
//плитка это сама матрица       
//	инцилизация полей которые относятся к самому классу  константы 
	    public static final int		TILE_SCALE		        = 8;   
		public static final int		TILE_IN_GAME_SCALE   	= 3;
		public static final int		SCALED_TILE_SIZE		= TILE_SCALE * TILE_IN_GAME_SCALE;
		public static final int		TILES_IN_WIDTH			= Game.WIDTH / SCALED_TILE_SIZE;
		public static final int		TILES_IN_HEIGHT			= Game.HEIGHT / SCALED_TILE_SIZE;
		public static final int		BONUS_DURATION			= 10000;
//		массив востановления орла 
		private static final int[]	ARRAY_TO_RESTORE_EAGLE	= new int[20];
    //сылкой наобьект в пакетах
		private Integer[][]			tileMap;
		private Map<TileType, Tile>	tiles;
		private List<Point>			grassCords;
		private int					count;
		private TextureAtlas		atlas;
		private Bonus				bonus;
		private Sprite				bonusSprite;
		private Point				bonusPoint;
		private boolean				hasBonus;
		private long				bonusCreatedTime;
		private boolean				eagleProtected;
		private InfoPanel			infoPanel;
		private boolean				eagleAlive;
//уравень получить текстуру и этап 
		public Level(TextureAtlas atlas, int stage) {
			tiles = new HashMap<>();
			count = 0;
			this.atlas = atlas;
			hasBonus = false;
			eagleProtected = false;
			infoPanel = new InfoPanel(atlas, stage);
			eagleAlive = true;
//  каждому блоку присваевываем характеристики и куда расположить плитку вырезать 
			tiles.put(TileType.BRICK, new Tile(atlas.cut(32 * TILE_SCALE, 0 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
					TILE_IN_GAME_SCALE, TileType.BRICK));
			tiles.put(TileType.METAL, new Tile(atlas.cut(32 * TILE_SCALE, 2 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
					TILE_IN_GAME_SCALE, TileType.METAL));
			tiles.put(TileType.WATER, new Tile(atlas.cut(34 * TILE_SCALE, 10 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
					TILE_IN_GAME_SCALE, TileType.WATER));
			tiles.put(TileType.GRASS, new Tile(atlas.cut(34 * TILE_SCALE, 4 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
					TILE_IN_GAME_SCALE, TileType.GRASS));
			tiles.put(TileType.EMPTY, new Tile(atlas.cut(36 * TILE_SCALE, 6 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
					TILE_IN_GAME_SCALE, TileType.EMPTY));
//			расположение и реконструкция орла
			tiles.put(TileType.UP_LEFT_EAGLE, new Tile(atlas.cut(38 * TILE_SCALE, 4 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
					TILE_IN_GAME_SCALE, TileType.UP_LEFT_EAGLE));
			tiles.put(TileType.UP_RIGHT_EAGLE, new Tile(atlas.cut(39 * TILE_SCALE, 4 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
					TILE_IN_GAME_SCALE, TileType.UP_RIGHT_EAGLE));
			tiles.put(TileType.DOWN_LEFT_EAGLE, new Tile(atlas.cut(38 * TILE_SCALE, 5 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
					TILE_IN_GAME_SCALE, TileType.DOWN_LEFT_EAGLE));
			tiles.put(TileType.DOWN_RIGHT_EAGLE,
//					убитый арел
					new Tile(atlas.cut(39 * TILE_SCALE, 5 * TILE_SCALE, TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE,
							TileType.DOWN_RIGHT_EAGLE));
			tiles.put(TileType.UP_LEFT_DEAD_EAGLE,
					new Tile(atlas.cut(40 * TILE_SCALE, 4 * TILE_SCALE, TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE,
							TileType.UP_LEFT_DEAD_EAGLE));
			tiles.put(TileType.UP_RIGHT_DEAD_EAGLE,
					new Tile(atlas.cut(41 * TILE_SCALE, 4 * TILE_SCALE, TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE,
							TileType.UP_RIGHT_DEAD_EAGLE));
			tiles.put(TileType.DOWN_LEFT_DEAD_EAGLE,
					new Tile(atlas.cut(40 * TILE_SCALE, 5 * TILE_SCALE, TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE,
							TileType.DOWN_LEFT_DEAD_EAGLE));
			tiles.put(TileType.DOWN_RIGHT_DEAD_EAGLE,
					new Tile(atlas.cut(41 * TILE_SCALE, 5 * TILE_SCALE, TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE,
							TileType.DOWN_RIGHT_DEAD_EAGLE));
			tiles.put(TileType.OTHER_WATER, new Tile(atlas.cut(33 * TILE_SCALE, 10 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
					TILE_IN_GAME_SCALE, TileType.OTHER_WATER));
//зарружаем карту расположения блоков в класс в карту плиток создав метод
//			вызавим статический метод они превязаны классу а не обьекту 
//			создаем сетку и соотносим размер спрайта
			tileMap = Utils.levelParser("res/lvl" + stage + ".lvl");
			grassCords = new ArrayList<>();
			for (int i = 0; i < tileMap.length; i++)
				for (int j = 0; j < tileMap[i].length; j++) {
					if (tileMap[i][j] == TileType.GRASS.numeric())
						grassCords.add(new Point(j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE));
				}
		}
//обновление расположения орла
		public void update(int tileX, int tileY) {
			if (tileMap[tileY][tileX] == TileType.DOWN_LEFT_EAGLE.numeric()
					|| tileMap[tileY][tileX] == TileType.DOWN_RIGHT_EAGLE.numeric()
					|| tileMap[tileY][tileX] == TileType.UP_LEFT_EAGLE.numeric()
					|| tileMap[tileY][tileX] == TileType.UP_RIGHT_EAGLE.numeric())
//				стираем
				destroyEagle();
			else
				tileMap[tileY][tileX] = TileType.EMPTY.numeric();
		}
//если орел мерт 
		private void destroyEagle() {
			for (int i = 0; i < tileMap.length; i++)
				for (int j = 0; j < tileMap[i].length; j++) {
					if (tileMap[i][j] == TileType.DOWN_LEFT_EAGLE.numeric())
						tileMap[i][j] = TileType.DOWN_LEFT_DEAD_EAGLE.numeric();

					else if (tileMap[i][j] == TileType.DOWN_RIGHT_EAGLE.numeric())
						tileMap[i][j] = TileType.DOWN_RIGHT_DEAD_EAGLE.numeric();

					else if (tileMap[i][j] == TileType.UP_LEFT_EAGLE.numeric())
						tileMap[i][j] = TileType.UP_LEFT_DEAD_EAGLE.numeric();

					else if (tileMap[i][j] == TileType.UP_RIGHT_EAGLE.numeric())
						tileMap[i][j] = TileType.UP_RIGHT_DEAD_EAGLE.numeric();
				}
			eagleAlive = false;
			Game.setGameOver();

		}
//отображать 
		public void render(Graphics2D g) {
//			подсчёт
			count = ++count % 20;
//робежатся по длине карты
			for (int i = 0; i < tileMap.length; i++)
//				по вертикале
				for (int j = 0; j < tileMap[i].length; j++) {
//					присвоить карте 
					Tile tile = tiles.get(TileType.fromNumeric(tileMap[i][j]));
//					если больше то это вода
					if (tile.type() == TileType.WATER && count < 10) {
//						получить тип плитки номер 5 и нарисовать её в маштабе 
						tiles.get(TileType.fromNumeric(5)).render(g, j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE);
					}
//				не трава мы залиаваем холст цветом 
					else {
						if (tile.type() != TileType.GRASS) {

							tile.render(g, j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE);

						}
					}
				}
//бонусы 
			if (bonus != null) {
				bonusSprite.render(g, bonusPoint.x, bonusPoint.y);
				if (System.currentTimeMillis() > bonusCreatedTime + BONUS_DURATION)
					removeBonus();
			}

			if (eagleProtected && System.currentTimeMillis() > bonusCreatedTime + BONUS_DURATION) {
				eagleProtected = false;
				restoreEagle();
			}
//выводим на понель бонусы
			infoPanel.renderInfoPanel(g);

		}
//заливаем травой 
		public void renderGrass(Graphics2D g) {
			for (Point p : grassCords) {
				tiles.get(TileType.GRASS).render(g, p.x, p.y);
			}
		}
//возвращаем карту 
		public Integer[][] getTileMap() {
			return tileMap;
		}
//бонусы получить 
		public void setBonus(Bonus bonus) {
			this.bonus = bonus;
			SpriteSheet sheet = new SpriteSheet(bonus.texture(atlas), Entity.SPRITES_PER_HEADING, Entity.SPRITE_SCALE);
			bonusSprite = new Sprite(sheet, Game.SCALE);
			Random rand = new Random();
			bonusPoint = new Point(rand.nextInt(12) * (int) (Entity.SPRITE_SCALE * Game.SCALE),
					(int) (rand.nextInt(12) * (Entity.SPRITE_SCALE * Game.SCALE)));
			hasBonus = true;
			bonusCreatedTime = System.currentTimeMillis();

		}
// имеет получил  бонус
		public boolean hasBonus() {
			return hasBonus;
		}
//нарисовать 
		public Rectangle2D getBonusRectangle() {
			return new Rectangle2D.Float(bonusPoint.x, bonusPoint.y, Entity.SPRITE_SCALE * Game.SCALE,
					Entity.SPRITE_SCALE * Game.SCALE);
		}
//установил 
		public Bonus getBonus() {
			return bonus;
		}
//удалить бонус 
		public void removeBonus() {
			bonus = null;
			bonusSprite = null;
			bonusPoint = null;
			hasBonus = false;
		}
//защита орла 
		public void protectEagle() {
			eagleProtected = true;
			bonusCreatedTime = System.currentTimeMillis();

			ARRAY_TO_RESTORE_EAGLE[0] = tileMap[25][10];
			ARRAY_TO_RESTORE_EAGLE[1] = tileMap[25][11];
			ARRAY_TO_RESTORE_EAGLE[2] = tileMap[24][10];
			ARRAY_TO_RESTORE_EAGLE[3] = tileMap[24][11];
			ARRAY_TO_RESTORE_EAGLE[4] = tileMap[23][10];
			ARRAY_TO_RESTORE_EAGLE[5] = tileMap[23][11];
			ARRAY_TO_RESTORE_EAGLE[6] = tileMap[22][10];
			ARRAY_TO_RESTORE_EAGLE[7] = tileMap[22][11];
			ARRAY_TO_RESTORE_EAGLE[8] = tileMap[23][12];
			ARRAY_TO_RESTORE_EAGLE[9] = tileMap[23][13];
			ARRAY_TO_RESTORE_EAGLE[10] = tileMap[22][12];
			ARRAY_TO_RESTORE_EAGLE[11] = tileMap[22][13];
			ARRAY_TO_RESTORE_EAGLE[12] = tileMap[25][15];
			ARRAY_TO_RESTORE_EAGLE[13] = tileMap[25][14];
			ARRAY_TO_RESTORE_EAGLE[14] = tileMap[24][15];
			ARRAY_TO_RESTORE_EAGLE[15] = tileMap[24][14];
			ARRAY_TO_RESTORE_EAGLE[16] = tileMap[23][15];
			ARRAY_TO_RESTORE_EAGLE[17] = tileMap[23][14];
			ARRAY_TO_RESTORE_EAGLE[18] = tileMap[22][15];
			ARRAY_TO_RESTORE_EAGLE[19] = tileMap[22][14];

			tileMap[25][10] = 2;
			tileMap[25][11] = 2;
			tileMap[24][10] = 2;
			tileMap[24][11] = 2;
			tileMap[23][10] = 2;
			tileMap[23][11] = 2;
			tileMap[22][10] = 2;
			tileMap[22][11] = 2;
			tileMap[23][12] = 2;
			tileMap[23][13] = 2;
			tileMap[22][12] = 2;
			tileMap[22][13] = 2;
			tileMap[25][15] = 2;
			tileMap[25][14] = 2;
			tileMap[24][15] = 2;
			tileMap[24][14] = 2;
			tileMap[23][15] = 2;
			tileMap[23][14] = 2;
			tileMap[22][15] = 2;
			tileMap[22][14] = 2;

		}
//востановление орла 
		private void restoreEagle() {
			tileMap[25][10] = ARRAY_TO_RESTORE_EAGLE[0];
			tileMap[25][11] = ARRAY_TO_RESTORE_EAGLE[1];
			tileMap[24][10] = ARRAY_TO_RESTORE_EAGLE[2];
			tileMap[24][11] = ARRAY_TO_RESTORE_EAGLE[3];
			tileMap[23][10] = ARRAY_TO_RESTORE_EAGLE[4];
			tileMap[23][11] = ARRAY_TO_RESTORE_EAGLE[5];
			tileMap[22][10] = ARRAY_TO_RESTORE_EAGLE[6];
			tileMap[22][11] = ARRAY_TO_RESTORE_EAGLE[7];
			tileMap[23][12] = ARRAY_TO_RESTORE_EAGLE[8];
			tileMap[23][13] = ARRAY_TO_RESTORE_EAGLE[9];
			tileMap[22][12] = ARRAY_TO_RESTORE_EAGLE[10];
			tileMap[22][13] = ARRAY_TO_RESTORE_EAGLE[11];
			tileMap[25][15] = ARRAY_TO_RESTORE_EAGLE[12];
			tileMap[25][14] = ARRAY_TO_RESTORE_EAGLE[13];
			tileMap[24][15] = ARRAY_TO_RESTORE_EAGLE[14];
			tileMap[24][14] = ARRAY_TO_RESTORE_EAGLE[15];
			tileMap[23][15] = ARRAY_TO_RESTORE_EAGLE[16];
			tileMap[23][14] = ARRAY_TO_RESTORE_EAGLE[17];
			tileMap[22][15] = ARRAY_TO_RESTORE_EAGLE[18];
			tileMap[22][14] = ARRAY_TO_RESTORE_EAGLE[19];

		}
//жив ли орел 
		public boolean isEagleAlive() {
			return eagleAlive;
		}

	}



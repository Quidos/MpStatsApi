package de.timmi6790.mineplex_stats_api.versions.v1.java.service;

import de.timmi6790.mineplex_stats_api.versions.v1.java.repository.JavaPlayerRepository;
import de.timmi6790.mineplex_stats_api.versions.v1.java.repository.models.GroupsModel;
import de.timmi6790.mineplex_stats_api.versions.v1.java.repository.models.PlayerStatsModule;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JavaPlayerService {
    private static final String SET_PLAYER_ID_NAME = "SET @playerId = (SELECT id FROM java_player WHERE player_name = :playerName LIMIT 1);";
    private static final String SET_PLAYER_ID_UUID = "SET @playerId = (SELECT id FROM java_player WHERE uuid = :uuid LIMIT 1);";

    private static final String GET_JAVA_PLAYER_STATS = "SELECT player.uuid uuid, " +
            "player.player_name player, " +
            "game.game_name game, " +
            "jboard.board_name board, " +
            "stat.stat_name, " +
            "(SELECT get_java_player_position_filter(save.leaderboard_save_id, @playerId)) position, " +
            "save.score, " +
            "UNIX_TIMESTAMP(datetime) unixtime " +
            " " +
            "FROM java_leaderboard board   " +
            "INNER JOIN java_leaderboard_save_id saveId ON saveId.id = (SELECT id FROM java_leaderboard_save_id " +
            "WHERE leaderboard_id = board.id ORDER BY ABS(TIMESTAMPDIFF(SECOND, java_leaderboard_save_id.datetime, '0')) LIMIT 1) " +
            "INNER JOIN java_leaderboard_save_new save ON save.leaderboard_save_id = saveId.id   " +
            "INNER JOIN java_player player ON player.id = save.player_id   " +
            "INNER JOIN java_stat stat ON stat.id = board.stat_id   " +
            "INNER JOIN java_game game ON game.id = board.game_id   " +
            "INNER JOIN java_board jboard ON jboard.id = board.board_id   " +
            "LEFT JOIN java_filter filter ON filter.player_id = player.id AND filter.leaderboard_id = board.id " +
            "WHERE player.id = @playerId " +
            "AND game.game_name = 'Global' " +
            "AND jboard.board_name = 'All' ";

    private static final String GET_JAVA_PLAYER_STATS_FILTER = GET_JAVA_PLAYER_STATS + "AND filter.id IS NULL;";

    private final JavaPlayerRepository javaPlayerRepository;

    public JavaPlayerService(final JavaPlayerRepository javaPlayerRepository) {
        this.javaPlayerRepository = javaPlayerRepository;
    }

    public Optional<PlayerStatsModule> getPlayerStats() {
        return Optional.empty();
    }

    public List<GroupsModel> getGroups() {
        return this.javaPlayerRepository.getGroups();
    }
}

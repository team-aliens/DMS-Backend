ALTER TABLE tbl_voting_option
DROP FOREIGN KEY tbl_voting_option_ibfk_1;
ALTER TABLE tbl_vote
DROP FOREIGN KEY tbl_vote_ibfk_1,
    DROP FOREIGN KEY tbl_vote_ibfk_2,
    DROP FOREIGN KEY tbl_vote_ibfk_3,
    DROP FOREIGN KEY tbl_vote_ibfk_4
